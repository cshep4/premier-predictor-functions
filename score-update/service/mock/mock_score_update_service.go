package mock

import (
	"github.com/stretchr/testify/mock"
)

type ScoreUpdateService struct {
	mock.Mock
}

func (_m ScoreUpdateService) ServiceName() string {
	return "MockScoreUpdate"
}

func (_m ScoreUpdateService) UpdateUserScores() error {
	ret := _m.Called()

	if ret.Get(0) == nil {
		return nil
	} else {
		return ret.Get(0).(error)
	}
}
