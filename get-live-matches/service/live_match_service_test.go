package service

import (
	"github.com/stretchr/testify/assert"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	"github.com/cshep4/premier-predictor-functions/common/redis/mock"
	"testing"
)

func TestRetrieveLiveMatchesWillRetrieveLiveMatchesFromRedisAndReturn(t *testing.T) {
	redis := new(mock.RedisRepository)
	liveMatchService := LiveMatchService{redis: redis}

	liveMatches := []LiveMatch{{}, {}}

	redis.On("GetAllLiveMatches").Return(liveMatches)
	redis.On("Close")

	result := liveMatchService.RetrieveLiveMatches()

	assert.Equal(t, liveMatches, result)
	redis.AssertCalled(t, "GetAllLiveMatches")
	redis.AssertCalled(t, "Close")
}
