package service

import (
	"fmt"
	"log"
	"os"
	. "github.com/cshep4/premier-predictor-functions/common/http"
	"github.com/cshep4/premier-predictor-functions/common/http/interfaces"
	"github.com/cshep4/premier-predictor-functions/send-message/domain"
)

type MessageService struct {
	http interfaces.HttpWrapper
}

func InjectMessageService() MessageService {
	return MessageService{http: HttpWrapper{}}
}

func (l MessageService) ServiceName() string {
	return "Message"
}

func (l MessageService) Send(message string) error {
	url := fmt.Sprintf(domain.URL, os.Getenv("ACCESS_TOKEN"))

	body := domain.SendApiBody{
		MessagingType: "UPDATE",
		Recipient: domain.Recipient{
			Id: os.Getenv("PSID"),
		},
		Message: domain.Message{
			Text: message,
		},
	}

	resp, err := l.http.Post(url, body)
	log.Println(resp)
	log.Println(err)

	return err
}
