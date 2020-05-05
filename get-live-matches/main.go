package main

import (
	"encoding/json"
	. "github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
	. "github.com/cshep4/premier-predictor-functions/common/factory"
	"github.com/cshep4/premier-predictor-functions/common/response"
	"github.com/cshep4/premier-predictor-functions/common/util"
	. "github.com/cshep4/premier-predictor-functions/get-live-matches/factory"
	"github.com/cshep4/premier-predictor-functions/get-live-matches/service/interfaces"
)

var liveMatchServiceFactory ServiceFactory

func Handler() (APIGatewayProxyResponse, error) {
	liveMatchService := liveMatchServiceFactory.Create().(interfaces.LiveMatchService)

	if liveMatches := liveMatchService.RetrieveLiveMatches(); len(liveMatches) > 0 {
		body, err := json.Marshal(liveMatches)
		util.CheckErr(err)

		return response.Ok(string(body))
	}

	return response.NotFound()
}

func main() {
	liveMatchServiceFactory = LiveMatchServiceFactory{}
	lambda.Start(Handler)
}