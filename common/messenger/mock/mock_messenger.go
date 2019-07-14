package mock

import (
	"github.com/stretchr/testify/mock"
)

type Messenger struct {
	mock.Mock
}

func (_m *Messenger) Send(message string) error {
	ret := _m.Called(message)

	var r error
	if rf, ok := ret.Get(0).(func(string) error); ok {
		r = rf(message)
	} else {
		if ret.Get(0) == nil {
			r = nil
		} else {
			r = ret.Get(0).(error)
		}
	}

	return r
}
