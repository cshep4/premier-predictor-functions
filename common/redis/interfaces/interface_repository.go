package interfaces

import (
	. "premier-predictor-functions/common/domain"
	. "time"
)

type RedisRepository interface {
	GetLiveMatch(id string) LiveMatch
	GetAllLiveMatches() []LiveMatch
	SetLiveMatch(liveMatch LiveMatch) error
	GetScoresUpdated() Time
	SetScoresUpdated() error
	Flush() error
	Close()
}
