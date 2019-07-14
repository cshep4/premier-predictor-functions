package service

import (
	"fmt"
	. "github.com/ahl5esoft/golang-underscore"
	_ "github.com/go-sql-driver/mysql"
	"log"
	. "premier-predictor-functions/common/api"
	iface "premier-predictor-functions/common/api/interfaces"
	. "premier-predictor-functions/common/domain"
	. "premier-predictor-functions/common/redis"
	"premier-predictor-functions/common/redis/interfaces"
	"time"
)

type LiveMatchCheckService struct {
	api   iface.ApiRequester
	redis interfaces.RedisRepository
}

func InjectLiveMatchCheckService() LiveMatchCheckService {
	return LiveMatchCheckService{
		redis: InjectRedisRepository(),
		api:   InjectApiRequester(),
	}
}

func (l LiveMatchCheckService) ServiceName() string {
	return "LiveMatchCheck"
}

func (l LiveMatchCheckService) UpdateLiveMatches() bool {
	defer l.redis.Close()

	//err := l.redis.Flush()
	//
	//if err != nil {
	//	log.Println("FLUSH ERROR - " + err.Error())
	//	return false
	//}

	matchFacts, apiErr := l.api.GetTodaysMatches()

	if apiErr == ErrNotFound {
		log.Println("API ERROR - " + apiErr.Error())
		return true
	}

	if apiErr != nil {
		log.Println("API ERROR - " + apiErr.Error())
		return false
	}

	playingMatches, ok := Where(matchFacts, isPlayingOrAboutToStart).([]MatchFacts)

	if !ok || len(playingMatches) < 1 {
		log.Println("No matches playing")
		return true
	}

	liveMatches := Map(playingMatches, mapToLiveMatch).([]LiveMatch)

	var saveErrs []error
	Each(liveMatches, func(m LiveMatch, _ int) {
		err := l.redis.SetLiveMatch(m)
		saveErrs = append(saveErrs, err)
	})

	if ok := Any(saveErrs, isNotNil); ok {
		log.Println("SAVE ERRORS:")
		log.Println(saveErrs)
		return false
	}

	return true
}

var isPlayingOrAboutToStart = func(m MatchFacts, _ int) bool { return isValidDateTime(m) && (m.IsPlaying() || m.IsAboutToStart()) }
var mapToLiveMatch = func(m MatchFacts, _ int) LiveMatch { return m.ToLiveMatch() }
var isNotNil = func(e error, _ int) bool { return e != nil }

var isValidDateTime = func(m MatchFacts) bool {
	dateTime := fmt.Sprintf("%sT%s", m.FormattedDate, m.Time)
	_, err := time.Parse("02.01.2006T15:04", dateTime)
	if err != nil {
		return false
	}

	return true
}
