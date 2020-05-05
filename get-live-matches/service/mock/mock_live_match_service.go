package mock

import (
	"github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
)

type LiveMatchService struct {
	mock.Mock
}

func (_m LiveMatchService) ServiceName() string {
	return "MockLiveMatch"
}

func (_m LiveMatchService) RetrieveLiveMatches() []LiveMatch {
	ret := _m.Called()

	return ret.Get(0).([]LiveMatch)
}