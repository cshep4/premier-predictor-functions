package mock

import (
	"github.com/stretchr/testify/mock"
)

type SmsSender struct {
	mock.Mock
}

func (_m *SmsSender) Send(message, phoneNumber string) error {
	ret := _m.Called(message, phoneNumber)

	var r error
	if rf, ok := ret.Get(0).(func(string, string) error); ok {
		r = rf(message, phoneNumber)
	} else {
		if ret.Get(0) == nil {
			r = nil
		} else {
			r = ret.Get(0).(error)
		}
	}

	return r
}
