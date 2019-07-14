package factory

import (
	. "premier-predictor-functions/common/service"
	. "premier-predictor-functions/heroku-liveness-probe/service"
)

type LivenessProbeServiceFactory struct {
}

func (l LivenessProbeServiceFactory) Create() Service {
	return InjectLivenessProbeService()
}
