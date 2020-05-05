package factory

import (
	. "github.com/cshep4/premier-predictor-functions/common/service"
	. "github.com/cshep4/premier-predictor-functions/send-message/service"
)

type MessageServiceFactory struct {
}

func (l MessageServiceFactory) Create() Service {
	return InjectMessageService()
}
