package mock

import (
	. "github.com/stretchr/testify/mock"
	. "premier-predictor-functions/common/domain"
	. "premier-predictor-functions/common/service"
	"premier-predictor-functions/get-live-matches/service/mock"
)

type LiveMatchServiceFactory struct {
	Mock
	result []LiveMatch
}

func MockLiveMatchServiceFactory(result []LiveMatch) LiveMatchServiceFactory {
	this := LiveMatchServiceFactory{}

	this.result = result

	return this

}

func (_m LiveMatchServiceFactory) Create() Service {
	liveMatchService := new(mock.LiveMatchService)
	liveMatchService.On("RetrieveLiveMatches").Return(_m.result)

	return liveMatchService

}
