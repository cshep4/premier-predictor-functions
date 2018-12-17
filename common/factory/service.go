package factory

import . "premier-predictor-functions/common/service"

type ServiceFactory interface {
	Create() Service
}