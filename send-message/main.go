package main

import (
	"encoding/json"
	"log"

	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"github.com/cshep4/premier-predictor-functions/common/response"

	"github.com/cshep4/premier-predictor-functions/send-message/domain"
	. "github.com/cshep4/premier-predictor-functions/send-message/factory"
)

var messageServiceFactory MessageServiceFactory

func Handler(req APIGatewayProxyRequest) (APIGatewayProxyResponse, error) {
	var message domain.Message

	err := json.Unmarshal([]byte(req.Body), &message)
	if err != nil {
		return response.BadRequest()
	}

	log.Println(req.Body)

	messageService := messageServiceFactory.Create()

	if err := messageService.Send(message.Text); err != nil {
		return response.InternalServerError()
	}

	return response.Ok("")
}

func main() {
	messageServiceFactory = MessageServiceFactory{}
	lambda.Start(Handler)
}
