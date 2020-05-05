package factory

import . "github.com/cshep4/premier-predictor-functions/common/service"

type ServiceFactory interface {
	Create() Service
}