package service

import (
	"errors"
	"github.com/stretchr/testify/assert"
	. "premier-predictor-functions/common/api"
	"premier-predictor-functions/common/api/mock"
	. "premier-predictor-functions/common/domain"
	mock2 "premier-predictor-functions/common/redis/mock"
	"strings"
	"testing"
	"time"
)

func TestUpdateLiveMatchesRetrievesMatchesFromApiMapsToLiveMatchAndStoresCurrentlyPlayingMatchesInRedis(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	dateTime := timeNowPlusMinutes(20)
	now := timeNowPlusMinutes(0)

	m1 := MatchFacts{LocalTeamName: "Test1", FormattedDate: now[0], Time: now[1], Status: "10"}
	m2 := MatchFacts{LocalTeamName: "Test2", FormattedDate: dateTime[0], Time: dateTime[1], Status: "FT"}

	matchFacts := []MatchFacts{m1, m2}

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return(matchFacts, nil)
	redisMock.On("SetLiveMatch", m1.ToLiveMatch()).Return(nil)

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertCalled(t, "SetLiveMatch", m1.ToLiveMatch())
	redisMock.AssertNotCalled(t, "SetLiveMatch", m2.ToLiveMatch())
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, true, result)
}

func TestUpdateLiveMatchesRetrievesMatchesFromApiMapsToLiveMatchAndStoresMatchesAboutToStartInRedis(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	dateTime := timeNowPlusMinutes(8)

	m := MatchFacts{FormattedDate: dateTime[0], Time: dateTime[1], Status: dateTime[1]}
	matchFacts := []MatchFacts{m}

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return(matchFacts, nil)
	redisMock.On("SetLiveMatch", m.ToLiveMatch()).Return(nil)

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertCalled(t, "SetLiveMatch", m.ToLiveMatch())
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, true, result)
}

//func TestUpdateLiveMatchesReturnsFalseIfThereIsAnErrorWithFlushingRedis(t *testing.T) {
//	apiMock, redisMock, liveMatchCheckService := setup()
//
//	redisMock.On("Flush").Return(errors.New(""))
//
//	result := liveMatchCheckService.UpdateLiveMatches()
//
//	apiMock.AssertNumberOfCalls(t, "GetTodaysMatches", 0)
//	redisMock.AssertNumberOfCalls(t, "SetLiveMatch", 0)
//	redisMock.AssertCalled(t, "Close")
//	assert.Equal(t, false, result)
//}

func TestUpdateLiveMatchesReturnsTrueIfNoMatchesAreToday(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return([]MatchFacts{}, nil)

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertNumberOfCalls(t, "SetLiveMatch", 0)
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, true, result)
}

func TestUpdateLiveMatchesReturnsTrueIfNoMatchesAreFound(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return([]MatchFacts{}, ErrNotFound)

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertNumberOfCalls(t, "SetLiveMatch", 0)
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, true, result)
}

func TestUpdateLiveMatchesReturnsTrueIfNoMatchesArePlayingOrComingUp(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	dateTime := timeNowPlusMinutes(20)

	matchFacts := []MatchFacts{{FormattedDate: dateTime[0], Time: dateTime[1], Status: "FT"}}

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return(matchFacts, nil)

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertNumberOfCalls(t, "SetLiveMatch", 0)
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, true, result)
}

func TestUpdateLiveMatchesReturnsFalseIfThereIsAProblemWithTheApiRequest(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return([]MatchFacts{}, errors.New(""))
	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertNumberOfCalls(t, "SetLiveMatch", 0)
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, false, result)
}

func TestUpdateLiveMatchesReturnsFalseIfThereIsAProblemWithStoringToRedis(t *testing.T) {
	apiMock, redisMock, liveMatchCheckService := setup()

	now := timeNowPlusMinutes(0)
	m1 := MatchFacts{LocalTeamName: "Test1", Status: "10", FormattedDate: now[0], Time: now[1]}

	matchFacts := []MatchFacts{m1}

	redisMock.On("Flush").Return(nil)
	apiMock.On("GetTodaysMatches").Return(matchFacts, nil)
	redisMock.On("SetLiveMatch", m1.ToLiveMatch()).Return(errors.New(""))

	result := liveMatchCheckService.UpdateLiveMatches()

	redisMock.AssertCalled(t, "SetLiveMatch", m1.ToLiveMatch())
	redisMock.AssertCalled(t, "Close")
	assert.Equal(t, false, result)
}

func setup() (*mock.ApiRequester, *mock2.RedisRepository, LiveMatchCheckService) {
	apiMock := new(mock.ApiRequester)
	redisMock := new(mock2.RedisRepository)

	liveMatchCheckService := LiveMatchCheckService{
		api:   apiMock,
		redis: redisMock,
	}

	redisMock.On("Close")

	return apiMock, redisMock, liveMatchCheckService
}

func timeNowPlusMinutes(minutes int) []string {
	twentyMinutesTime := time.Now().UTC().Add(time.Minute * time.Duration(minutes)).Format("02.01.2006T15:04")
	dateTime := strings.Split(twentyMinutesTime, "T")
	return dateTime
}
