package main

import (
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	. "github.com/cshep4/premier-predictor-functions/common/factory"
	"github.com/cshep4/premier-predictor-functions/common/response"
	. "github.com/cshep4/premier-predictor-functions/score-update/constant"
	. "github.com/cshep4/premier-predictor-functions/score-update/factory"
	"github.com/cshep4/premier-predictor-functions/score-update/service/interfaces"
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