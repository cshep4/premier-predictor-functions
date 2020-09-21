package interfaces

import (
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "time"
)

type RedisRepository interface {
	GetLiveMatch(id string) LiveMatch
	GetAllLiveMatches() ([]LiveMatch, error)
	SetLiveMatch(liveMatch LiveMatch) error
	GetScoresUpdated() Time
	SetScoresUpdated() error
	SetIdempotency(key string) error
	CheckIdempotency(key string) (bool, error)
	Flush() error
	Close()
}
