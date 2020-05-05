package factory

import (
	. "github.com/cshep4/premier-predictor-functions/common/service"
	. "github.com/cshep4/premier-predictor-functions/heroku-liveness-probe/service"
)

type LivenessProbeServiceFactory struct {
}

func (l LivenessProbeServiceFactory) Create() Service {
	return InjectLivenessProbeService()
}
