package main

import (
	"encoding/json"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	emailer "github.com/cshep4/premier-predictor-functions/common/email"
	"github.com/cshep4/premier-predictor-functions/common/redis"
	email "github.com/cshep4/premier-predictor-functions/match-data-provider/internal/service"
	"net/http"
)

var service email.Service

func main() {
	service = email.NewService(
		redis.InjectRedisRepository(),
		emailer.InjectEmailer(),
	)

	lambda.Start(handler)
}

func handler(req events.APIGatewayProxyRequest) (*events.APIGatewayProxyResponse, error) {
	var emailReq email.SendEmailRequest
	err := json.Unmarshal([]byte(req.Body), &emailReq)
	if err != nil {
		return nil, err
	}

	err = service.SendEmail(emailReq)
	if err != nil {
		return nil, err
	}

	return &events.APIGatewayProxyResponse{
		StatusCode: http.StatusOK,
	}, nil
}
