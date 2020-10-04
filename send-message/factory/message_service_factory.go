package factory

import (
	"github.com/cshep4/premier-predictor-functions/send-message/service"
	"github.com/cshep4/premier-predictor-functions/send-message/service/interfaces"
)

type MessageServiceFactory struct {
}

func (l MessageServiceFactory) Create() interfaces.MessageService {
	return service.InjectMessageService()
}
