package factory

import (
	. "premier-predictor-functions/common/service"
	. "premier-predictor-functions/score-update/service"
)

type ScoreUpdateServiceFactory struct {
}

func (l ScoreUpdateServiceFactory) Create() Service {
	return InjectScoreUpdateService()
}