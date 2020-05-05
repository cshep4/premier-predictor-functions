package interfaces

import . "github.com/cshep4/premier-predictor-functions/common/domain"

type ApiRequester interface {
	GetTodaysMatches() ([]MatchFacts, error)
}
