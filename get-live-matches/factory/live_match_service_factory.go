package factory

import (
	. "premier-predictor-functions/common/service"
	. "premier-predictor-functions/get-live-matches/service"
)

type LiveMatchServiceFactory struct {
}

func (l LiveMatchServiceFactory) Create() Service {
	return InjectLiveMatchService()
}