package mock

import (
	"github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
)

type ApiRequester struct {
	mock.Mock
}

func (_m *ApiRequester) GetTodaysMatches() ([]MatchFacts, error) {
	ret := _m.Called()

	var r1 []MatchFacts
	var r2 error
	if ret.Get(0) == nil {
		r1 = nil
	} else {
		r1 = ret.Get(0).([]MatchFacts)
	}

	if ret.Get(1) == nil {
		r2 = nil
	} else {
		r2 = ret.Get(1).(error)
	}

	return r1, r2
}
