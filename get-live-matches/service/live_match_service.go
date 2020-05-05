package service

import (
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "github.com/cshep4/premier-predictor-functions/common/redis"
	"github.com/cshep4/premier-predictor-functions/common/redis/interfaces"
)

type LiveMatchService struct {
	redis interfaces.RedisRepository
}

func InjectLiveMatchService() LiveMatchService {
	return LiveMatchService{
		redis: InjectRedisRepository(),
	}
}

func (l LiveMatchService) ServiceName() string {
	return "LiveMatch"
}

func (l LiveMatchService) RetrieveLiveMatches() []LiveMatch {
	defer l.redis.Close()

	return l.redis.GetAllLiveMatches()
}
