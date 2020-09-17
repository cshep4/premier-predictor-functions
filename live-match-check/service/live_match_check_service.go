package service

import (
	"fmt"
	. "github.com/ahl5esoft/golang-underscore"
	"log"
	. "github.com/cshep4/premier-predictor-functions/common/api"
	iface "github.com/cshep4/premier-predictor-functions/common/api/interfaces"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "github.com/cshep4/premier-predictor-functions/common/redis"
	"github.com/cshep4/premier-predictor-functions/common/redis/interfaces"
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

	var playingMatches []MatchFacts
	for _, m := range matchFacts {
		playing, err := isPlayingOrAboutToStart(m)
		if err != nil {
			log.Println("error checking matches - " + err.Error())
			return true
		}
		if playing {
			playingMatches = append(playingMatches, m)
		}
	}

	if len(playingMatches) < 1 {
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

func isPlayingOrAboutToStart(m MatchFacts) (bool, error) {
	today, err := isMatchToday(m)
	if err != nil {
		return false, err
	}

	return today && isValidDateTime(m) && (m.IsPlaying() || m.IsAboutToStart()), nil
}

func isMatchToday(m MatchFacts) (bool, error) {
	dateTime := fmt.Sprintf("%sT%s", m.FormattedDate, m.Time)
	matchTime, err := time.Parse("02.01.2006T15:04", dateTime)
	if err != nil {
		return false, err
	}

	y1, m1, d1 := matchTime.Date()
	y2, m2, d2 := time.Now().Date()

	return y1 == y2 && m1 == m2 && d1 == d2, nil
}
