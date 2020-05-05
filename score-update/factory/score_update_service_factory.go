package factory

import (
	. "github.com/cshep4/premier-predictor-functions/common/service"
	. "github.com/cshep4/premier-predictor-functions/score-update/service"
)

type ScoreUpdateServiceFactory struct {
}

func (l ScoreUpdateServiceFactory) Create() Service {
	return InjectScoreUpdateService()
}