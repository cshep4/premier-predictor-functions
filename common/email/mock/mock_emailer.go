package mock

import (
	"github.com/stretchr/testify/mock"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
)

type Emailer struct {
	mock.Mock
}

func (_m *Emailer) Send(args *EmailArgs) error {
	ret := _m.Called(args)

	var r2 error
	if rf, ok := ret.Get(0).(func(*EmailArgs) error); ok {
		r2 = rf(args)
	} else {
		if ret.Get(0) == nil {
			r2 = nil
		} else {
			r2 = ret.Get(0).(error)
		}
	}

	return r2
}