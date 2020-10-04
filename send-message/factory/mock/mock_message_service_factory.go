package mock

import (
	. "github.com/stretchr/testify/mock"

	"github.com/cshep4/premier-predictor-functions/send-message/service/interfaces"
	"github.com/cshep4/premier-predictor-functions/send-message/service/mock"
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

func (_m MessageServiceFactory) Create() interfaces.MessageService {
	messageService := new(mock.MessageService)
	messageService.On("Send", _m.input).Return(_m.result)

	return messageService

}
