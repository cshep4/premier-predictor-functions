package mock

import (
	"github.com/stretchr/testify/mock"
)

type LiveMatchCheckService struct {
	mock.Mock
}

func (_m LiveMatchCheckService) ServiceName() string {
	return "MockLiveMatchCheck"
}

func (_m LiveMatchCheckService) UpdateLiveMatches() bool {
	ret := _m.Called()

	return ret.Get(0).(bool)
}