package mock

import (
	. "github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/service"
	"github.com/cshep4/premier-predictor-functions/score-update/service/mock"
)

type ScoreUpdateServiceFactory struct {
	Mock
	result error
}

func MockScoreUpdateServiceFactory(result error) ScoreUpdateServiceFactory {
	this := ScoreUpdateServiceFactory{}

	this.result = result

	return this

}

func (_m ScoreUpdateServiceFactory) Create() Service {
	liveMatchCheckService := new(mock.ScoreUpdateService)
	liveMatchCheckService.On("UpdateUserScores").Return(_m.result)

	return liveMatchCheckService

}
