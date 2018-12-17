package main

import (
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	. "premier-predictor-functions/common/factory"
	"premier-predictor-functions/common/response"
	. "premier-predictor-functions/score-update/constant"
	. "premier-predictor-functions/score-update/factory"
	"premier-predictor-functions/score-update/service/interfaces"
)

var scoreUpdateServiceFactory ServiceFactory

func Handler() (APIGatewayProxyResponse, error) {
	scoreUpdateService := scoreUpdateServiceFactory.Create().(interfaces.ScoreUpdateService)

	err := scoreUpdateService.UpdateUserScores()

	if err == ErrScoresAlreadyUpdated || err == ErrScoresDoNotNeedUpdating {
		return response.NotModified()
	}

	if err == ErrUpdatingScores {
		return response.InternalServerError()
	}

	return response.Ok("")
}

func main() {
	scoreUpdateServiceFactory = ScoreUpdateServiceFactory{}
	lambda.Start(Handler)
}