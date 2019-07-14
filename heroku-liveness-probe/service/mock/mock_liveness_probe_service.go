package mock

import (
	"github.com/stretchr/testify/mock"
)

type LivenessProbeService struct {
	mock.Mock
}

func (_m LivenessProbeService) ServiceName() string {
	return "MockLivenessProbe"
}

func (_m LivenessProbeService) Check() error {
	ret := _m.Called()

	return ret.Error(0)
}
