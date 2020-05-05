package main

import (
	"context"
	"encoding/json"
	"errors"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	emailer "github.com/cshep4/premier-predictor-functions/common/email"
	"github.com/cshep4/premier-predictor-functions/common/redis"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/service"
	"log"
)

var service email.Service

func main() {
	service = email.NewService(
		redis.InjectRedisRepository(),
		emailer.InjectEmailer(),
	)

	lambda.Start(handler)
}

func handler(ctx context.Context, sqsEvent events.SQSEvent) error {
	if len(sqsEvent.Records) == 0 {
		return errors.New("no SQS message passed to function")
	}

	for _, msg := range sqsEvent.Records {
		var req email.SendEmailRequest
		err := json.Unmarshal([]byte(msg.Body), &req)
		if err != nil {
			log.Printf("invalid body: %s", err)
			continue
		}

		err = service.SendEmail(req)
		if err != nil {
			return err
		}
	}

	return nil
}
