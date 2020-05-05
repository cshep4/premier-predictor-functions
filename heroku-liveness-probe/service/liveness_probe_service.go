package service

import (
	"context"
	"fmt"
	"github.com/avast/retry-go"
	"github.com/heroku/heroku-go/v3"
	"log"
	"net/http"
	"os"
	. "github.com/cshep4/premier-predictor-functions/common/http"
	. "github.com/cshep4/premier-predictor-functions/common/messenger"
	"time"
)

const premierPredictorAppName = "premierpredictor"

type LivenessProbeService struct {
	messenger Messenger
	http      HttpWrapper
}

func InjectLivenessProbeService() LivenessProbeService {
	return LivenessProbeService{
		messenger: InjectMessenger(),
		http:      InjectHttpWrapper(),
	}
}

func (l LivenessProbeService) ServiceName() string {
	return "LivenessProbe"
}

//TODO - Add tests & refactor
func (l LivenessProbeService) Check() error {
	heroku.DefaultTransport.BearerToken = os.Getenv("HEROKU_API_KEY")
	h := heroku.NewService(heroku.DefaultClient)

	dynos, err := h.DynoList(context.TODO(), premierPredictorAppName, &heroku.ListRange{Field: "name"})
	if err != nil {
		return err
	}

	dyno := dynos[0]

	if dyno.State == "starting" {
		log.Println("Dyno Starting")
		return nil
	}

	if dyno.State != "up" {
		log.Printf("Dyno not available. Heroku server state: %s", dyno.State)

		message := fmt.Sprintf("Heroku server state: %s - Restarting", dyno.State)
		l.messenger.Send(message)

		_, err := h.DynoRestart(context.TODO(), premierPredictorAppName, dyno.Name)

		return err
	}

	var resp *http.Response

	err = retry.Do(
		func() error {
			resp, err = l.http.GetWithTimeout(os.Getenv("HEROKU_HEALTH_URL"), time.Duration(1*time.Second))
			if err != nil {
				return err
			}

			return nil
		},
		retry.Attempts(2),
	)

	if err != nil || resp.StatusCode != http.StatusOK {
		message := "Server not available, liveness check failed - Restarting."

		if err != nil {
			message = fmt.Sprintf("Server not available, liveness check failed - Restarting. Error: %s", err)
		} else if resp != nil && resp.StatusCode != http.StatusOK {
			message = fmt.Sprintf("Server not available, liveness check failed - Restarting. Response: %s", resp.Status)
		}

		log.Println(message)

		l.messenger.Send(message)

		_, err := h.DynoRestart(context.TODO(), premierPredictorAppName, dyno.Name)

		return err
	}

	return nil
}
