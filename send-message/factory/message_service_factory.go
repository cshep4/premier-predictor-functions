package factory

import (
	. "premier-predictor-functions/common/service"
	. "premier-predictor-functions/send-message/service"
)

type MessageServiceFactory struct {
}

func (l MessageServiceFactory) Create() Service {
	return InjectMessageService()
}
