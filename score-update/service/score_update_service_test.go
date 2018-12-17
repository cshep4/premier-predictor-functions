package service

import (
	"errors"
	"github.com/stretchr/testify/assert"
	. "net/http"
	"premier-predictor-functions/common/api/mock"
	. "premier-predictor-functions/common/domain"
	mock3 "premier-predictor-functions/common/email/mock"
	mock4 "premier-predictor-functions/common/http/mock"
	mock2 "premier-predictor-functions/common/redis/mock"
	. "premier-predictor-functions/score-update/constant"
	"testing"
	"time"
)

func TestUpdateUserScoresReturnsNilIfScoresAreUpdatedSuccessfully(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "FT", FormattedDate: time.Now().Format("02.01.2006")}}

	emailArgs1 := getEmailArgs("Updating user scores - START")
	emailArgs2 := getEmailArgs("Updating user scores - END")

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs1).Return(nil)
	emailer.On("Send", emailArgs2).Return(nil)
	http.On("Put", SCORE_UPDATE_URL).Return(&Response{}, nil)
	redis.On("SetScoresUpdated").Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs1)
	emailer.AssertCalled(t, "Send", emailArgs2)
	http.AssertCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertCalled(t, "SetScoresUpdated")
	assert.Equal(t, nil, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorIfThereAreNoMatchesToday(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "FT", FormattedDate: time.Now().AddDate(0, 0, -1).Format("02.01.2006")}}
	emailArgs := getEmailArgs(ErrScoresDoNotNeedUpdating.Error())

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresDoNotNeedUpdating, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorAndSendsEmailIfAllGamesHaveNotFinished(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "88", FormattedDate: time.Now().AddDate(0, 0, -1).Format("02.01.2006")}}
	emailArgs := getEmailArgs(ErrScoresDoNotNeedUpdating.Error())

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresDoNotNeedUpdating, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorAndSendsEmailIfThereIsAnApiError(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	emailArgs := getEmailArgs(ErrUpdatingScores.Error())

	api.On("GetTodaysMatches").Return([]MatchFacts{}, errors.New(""))
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrUpdatingScores, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorIfScoresHaveAlreadyBeenUpdatedToday(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	emailArgs := getEmailArgs(ErrScoresAlreadyUpdated.Error())

	redis.On("GetScoresUpdated").Return(time.Now())
	emailer.On("Send", emailArgs).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	api.AssertNotCalled(t, "GetTodaysMatches")
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresAlreadyUpdated, result)
}

func TestUpdateUserScoresReturnsErrorIfScoresAreNotUpdatedSuccessfully(t *testing.T) {
	api, redis, emailer, http, scoreUpdateService := setup()

	emailArgs1 := getEmailArgs("Updating user scores - START")
	emailArgs2 := getEmailArgs(ErrUpdatingScores.Error())

	api.On("GetTodaysMatches").Return([]MatchFacts{}, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs1).Return(nil)
	emailer.On("Send", emailArgs2).Return(nil)
	http.On("Put", SCORE_UPDATE_URL).Return(nil, errors.New(""))

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs1)
	emailer.AssertCalled(t, "Send", emailArgs2)
	http.AssertCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrUpdatingScores, result)
}

func setup() (*mock.ApiRequester, *mock2.RedisRepository, *mock3.Emailer, *mock4.HttpWrapper, ScoreUpdateService) {
	apiMock := new(mock.ApiRequester)
	redisMock := new(mock2.RedisRepository)
	emailerMock := new(mock3.Emailer)
	httpMock := new(mock4.HttpWrapper)

	scoreUpdateService := ScoreUpdateService{
		api:     apiMock,
		redis:   redisMock,
		emailer: emailerMock,
		http:    httpMock,
	}

	redisMock.On("Close")

	return apiMock, redisMock, emailerMock, httpMock, scoreUpdateService
}
