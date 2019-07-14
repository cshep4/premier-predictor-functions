package main

import (
	"encoding/json"
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"log"
	. "premier-predictor-functions/common/factory"
	"premier-predictor-functions/common/response"
	"premier-predictor-functions/send-message/domain"
	. "premier-predictor-functions/send-message/factory"
	"premier-predictor-functions/send-message/service/interfaces"
)

var messageServiceFactory ServiceFactory

func Handler(req APIGatewayProxyRequest) (APIGatewayProxyResponse, error) {
	var message domain.Message

	err := json.Unmarshal([]byte(req.Body), &message)
	if err != nil {
		return response.BadRequest()
	}

	log.Println(req.Body)

	messageService := messageServiceFactory.Create().(interfaces.MessageService)

	if err := messageService.Send(message.Text); err != nil {
		return response.InternalServerError()
	}

	return response.Ok("")
}

func main() {
	messageServiceFactory = MessageServiceFactory{}
	lambda.Start(Handler)
}
