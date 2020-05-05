package mock

import (
	. "github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/service"
	"github.com/cshep4/premier-predictor-functions/heroku-liveness-probe/service/mock"
)

type LivenessProbeServiceFactory struct {
	Mock
	result error
}

func MockLivenessProbeServiceFactory(result error) LivenessProbeServiceFactory {
	this := LivenessProbeServiceFactory{}

	this.result = result

	return this

}

func (_m LivenessProbeServiceFactory) Create() Service {
	livenessProbeService := new(mock.LivenessProbeService)
	livenessProbeService.On("Check").Return(_m.result)

	return livenessProbeService

}