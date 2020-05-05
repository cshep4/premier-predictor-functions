package interfaces

import . "github.com/cshep4/premier-predictor-functions/common/domain"

type LiveMatchService interface {
	ServiceName() string
	RetrieveLiveMatches() []LiveMatch
}

