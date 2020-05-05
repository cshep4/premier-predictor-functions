package factory

import (
	. "github.com/cshep4/premier-predictor-functions/common/service"
	. "github.com/cshep4/premier-predictor-functions/get-live-matches/service"
)

type LiveMatchServiceFactory struct {
}

func (l LiveMatchServiceFactory) Create() Service {
	return InjectLiveMatchService()
}