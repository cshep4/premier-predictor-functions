package factory

import (
	. "github.com/cshep4/premier-predictor-functions/common/service"
	. "github.com/cshep4/premier-predictor-functions/live-match-check/service"
)

type LiveMatchCheckServiceFactory struct {
}

func (l LiveMatchCheckServiceFactory) Create() Service {
	return InjectLiveMatchCheckService()
}