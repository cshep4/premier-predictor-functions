package mock

import (
	"github.com/stretchr/testify/mock"
)

type MessageService struct {
	mock.Mock
}

func (_m MessageService) ServiceName() string {
	return "MockMessage"
}

func (_m MessageService) Send(message string) error {
	ret := _m.Called(message)

	return ret.Error(0)
}
