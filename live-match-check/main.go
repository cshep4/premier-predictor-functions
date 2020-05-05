package main

import (
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	. "github.com/cshep4/premier-predictor-functions/common/factory"
	"github.com/cshep4/premier-predictor-functions/common/response"
	. "github.com/cshep4/premier-predictor-functions/live-match-check/factory"
	"github.com/cshep4/premier-predictor-functions/live-match-check/service/interfaces"
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