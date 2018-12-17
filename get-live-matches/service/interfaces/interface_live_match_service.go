package interfaces

import . "premier-predictor-functions/common/domain"

type LiveMatchService interface {
	ServiceName() string
	RetrieveLiveMatches() []LiveMatch
}

