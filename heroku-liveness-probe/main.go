package main

import (
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	"log"
	. "github.com/cshep4/premier-predictor-functions/common/factory"
	"github.com/cshep4/premier-predictor-functions/common/response"
	. "github.com/cshep4/premier-predictor-functions/heroku-liveness-probe/factory"
	"github.com/cshep4/premier-predictor-functions/heroku-liveness-probe/service/interfaces"
)

var livenessProbeServiceFactory ServiceFactory

func Handler() (APIGatewayProxyResponse, error) {
	livenessProbeService := livenessProbeServiceFactory.Create().(interfaces.LivenessProbeService)

	if err := livenessProbeService.Check(); err != nil {
		log.Println(err)

		return response.InternalServerError()
	}

	return response.Ok("")
}

func main() {
	livenessProbeServiceFactory = LivenessProbeServiceFactory{}
	lambda.Start(Handler)
}
