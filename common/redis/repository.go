package redis

import (
	"fmt"
	"github.com/mediocregopher/radix/v3"
	"log"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	"github.com/cshep4/premier-predictor-functions/common/util"
	. "time"
)

type Repository struct {
	redis radix.Conn
}

func InjectRedisRepository() Repository {
	return Repository{Dial()}
}

func (r Repository) GetAllLiveMatches() []LiveMatch {
	var keys []string

	key := LIVE_MATCH + ":*"

	err := r.redis.Do(radix.Cmd(&keys, KEYS, key))
	util.CheckErr(err)

	var liveMatches []LiveMatch

	for _, k := range keys {
		var liveMatch LiveMatch

		err = r.redis.Do(radix.Cmd(&liveMatch, GET_ALL, k))
		util.CheckErr(err)

		liveMatches = append(liveMatches, liveMatch)
	}

	return liveMatches
}

func (r Repository) GetLiveMatch(id string) LiveMatch {
	var liveMatch LiveMatch

	key := LIVE_MATCH + ":" + id

	err := r.redis.Do(radix.Cmd(&liveMatch, GET_ALL, key))
	util.CheckErr(err)

	return liveMatch
}

func (r Repository) SetLiveMatch(liveMatch LiveMatch) error {
	m := map[string]string{
		ID:             liveMatch.Id,
		FORMATTED_DATE: liveMatch.FormattedDate,
		HOME_TEAM:      liveMatch.HomeTeam,
		AWAY_TEAM:      liveMatch.AwayTeam,
		CLASS:          LIVE_MATCH_CLASS,
	}

	r.addToSetOfIds(LIVE_MATCH, liveMatch.Id)

	key := LIVE_MATCH + ":" + liveMatch.Id

	return r.redis.Do(radix.FlatCmd(nil, SET, key, m))
}

func (r Repository) GetScoresUpdated() Time {
	var m map[string]string

	key := SCORES_UPDATED + ":" + SCORES_UPDATED_ID

	err := r.redis.Do(radix.Cmd(&m, GET_ALL, key))
	util.CheckErr(err)

	lastUpdated, e := Parse("2006-01-02", m[LAST_UPDATED])
	util.CheckErr(e)

	return lastUpdated
}

func (r Repository) SetScoresUpdated() error {
	m := map[string]string{
		ID:           SCORES_UPDATED_ID,
		LAST_UPDATED: Now().Format("2006-01-02"),
		CLASS:        SCORES_UPDATED_CLASS,
	}

	r.addToSetOfIds(SCORES_UPDATED, SCORES_UPDATED_ID)

	key := SCORES_UPDATED + ":" + SCORES_UPDATED_ID

	return r.redis.Do(radix.FlatCmd(nil, SET, key, m))
}

func (r Repository) addToSetOfIds(key string, value string) error {
	return r.redis.Do(radix.FlatCmd(nil, ADD_TO_SET, key, value))
}

func (r Repository) SetIdempotency(key string) error {
	k := fmt.Sprintf(IDEMPOTENCY, key)
	return r.redis.Do(radix.FlatCmd(nil, SET_WITH_TTL, k, FOUR_DAYS, key))
}

func (r Repository) CheckIdempotency(key string) (bool, error) {
	k := fmt.Sprintf(IDEMPOTENCY, key)

	var value string
	err := r.redis.Do(radix.Cmd(&value, GET, k))
	if err != nil {
		return false, err
	}

	return value != "", nil
}

func (r Repository) Flush() error {
	var keys []string
	r.redis.Do(radix.Cmd(&keys, "KEYS", LIVE_MATCH+"*"))

	if len(keys) == 0 {
		log.Println("No live matches to flush")
		return nil
	}

	log.Println("Flushing Keys:")
	log.Println(keys)

	return r.redis.Do(radix.Cmd(nil, "DEL", keys...))
}

func (r Repository) Close() {
	r.redis.Close()
}
