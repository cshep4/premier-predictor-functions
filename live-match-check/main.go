package main

import (
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	. "premier-predictor-functions/common/factory"
	"premier-predictor-functions/common/response"
	. "premier-predictor-functions/live-match-check/factory"
	"premier-predictor-functions/live-match-check/service/interfaces"
)

var liveMatchCheckServiceFactory ServiceFactory

func Handler() (APIGatewayProxyResponse, error) {
	liveMatchCheckService := liveMatchCheckServiceFactory.Create().(interfaces.LiveMatchCheckService)

	if !liveMatchCheckService.UpdateLiveMatches() {
		return response.InternalServerError()
	}

	return response.Ok("")
}

func main() {
	liveMatchCheckServiceFactory = LiveMatchCheckServiceFactory{}
	lambda.Start(Handler)
}