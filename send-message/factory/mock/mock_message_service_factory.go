package mock

import (
	. "github.com/stretchr/testify/mock"
	. "premier-predictor-functions/common/service"
	"premier-predictor-functions/send-message/service/mock"
)

type MessageServiceFactory struct {
	Mock
	input  string
	result error
}

func MockMessageServiceFactory(input string, result error) MessageServiceFactory {
	this := MessageServiceFactory{}

	this.result = result
	this.input = input

	return this

}

func (_m MessageServiceFactory) Create() Service {
	messageService := new(mock.MessageService)
	messageService.On("Send", _m.input).Return(_m.result)

	return messageService

}
