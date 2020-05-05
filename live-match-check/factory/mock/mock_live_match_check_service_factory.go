package mock

import (
	. "github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/service"
	"github.com/cshep4/premier-predictor-functions/live-match-check/service/mock"
)

type LiveMatchCheckServiceFactory struct {
	Mock
	result bool
}

func MockLiveMatchCheckServiceFactory(result bool) LiveMatchCheckServiceFactory {
	this := LiveMatchCheckServiceFactory{}

	this.result = result

	return this

}

func (_m LiveMatchCheckServiceFactory) Create() Service {
	liveMatchCheckService := new(mock.LiveMatchCheckService)
	liveMatchCheckService.On("UpdateLiveMatches").Return(_m.result)

	return liveMatchCheckService

}
