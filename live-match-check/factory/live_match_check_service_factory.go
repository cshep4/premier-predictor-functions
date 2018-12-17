package factory

import (
	. "premier-predictor-functions/common/service"
	. "premier-predictor-functions/live-match-check/service"
)

type LiveMatchCheckServiceFactory struct {
}

func (l LiveMatchCheckServiceFactory) Create() Service {
	return InjectLiveMatchCheckService()
}