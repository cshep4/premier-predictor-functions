package aws

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"

	"github.com/aws/aws-lambda-go/events"
	"github.com/cshep4/lambda-go/log"

	"github.com/cshep4/premier-predictor-functions/league-user/internal/league"
)

type (
	Service interface {
		CreateLeagueUser(ctx context.Context, user league.User) error
	}

	Handler struct {
		Service Service
	}
)

func (h *Handler) CreateLeagueUser(ctx context.Context, sqsEvent events.SQSEvent) error {
	if len(sqsEvent.Records) == 0 {
		return errors.New("no sqs message passed to function")
	}

	for _, msg := range sqsEvent.Records {
		var user league.User
		err := json.Unmarshal([]byte(msg.Body), &user)
		if err != nil {
			log.Error(ctx, "invalid_msg_body", log.ErrorParam(err))
			continue
		}

		err = h.Service.CreateLeagueUser(ctx, user)
		if err != nil {
			log.Error(ctx, "err_creating_league_user",
				log.SafeParam("user_id", user.ID),
				log.ErrorParam(err),
			)

			return fmt.Errorf("create_league_user: %w", err)
		}

		log.Info(ctx, "league_user_created", log.SafeParam("user_id", user.ID))
	}

	return nil
}
