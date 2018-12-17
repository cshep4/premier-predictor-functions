package mock

import (
	"github.com/stretchr/testify/mock"
	. "net/http"
)

type HttpWrapper struct {
	mock.Mock
}

func (_m *HttpWrapper) Get(url string) (*Response, error) {
	ret := _m.Called(url)

	var r1 *Response
	var r2 error
	if rf, ok := ret.Get(0).(func(string) (*Response, error)); ok {
		r1, r2 = rf(url)
	} else {
		if ret.Get(0) == nil {
			r1 = nil
		} else {
			r1 = ret.Get(0).(*Response)
		}

		if ret.Get(1) == nil {
			r2 = nil
		} else {
			r2 = ret.Get(1).(error)
		}
	}

	return r1, r2
}

func (_m *HttpWrapper) Put(url string, data interface{}) (*Response, error) {
	ret := _m.Called(url)

	var r1 *Response
	var r2 error
	if rf, ok := ret.Get(0).(func(string) (*Response, error)); ok {
		r1, r2 = rf(url)
	} else {
		if ret.Get(0) == nil {
			r1 = nil
		} else {
			r1 = ret.Get(0).(*Response)
		}

		if ret.Get(1) == nil {
			r2 = nil
		} else {
			r2 = ret.Get(1).(error)
		}
	}

	return r1, r2
}
