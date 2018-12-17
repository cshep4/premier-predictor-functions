package interfaces

import . "premier-predictor-functions/common/domain"

type ApiRequester interface {
	GetTodaysMatches() ([]MatchFacts, error)
}
