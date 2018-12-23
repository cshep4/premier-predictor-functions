package service

import (
	"errors"
	"github.com/stretchr/testify/assert"
	. "net/http"
	"os"
	"premier-predictor-functions/common/api/mock"
	. "premier-predictor-functions/common/domain"
	mock3 "premier-predictor-functions/common/email/mock"
	mock4 "premier-predictor-functions/common/http/mock"
	mock2 "premier-predictor-functions/common/redis/mock"
	mock5 "premier-predictor-functions/common/sms/mock"
	. "premier-predictor-functions/score-update/constant"
	"testing"
	"time"
)

const MOBILE_NUMBER = "mobile number"

func TestUpdateUserScoresReturnsNilIfScoresAreUpdatedSuccessfully(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "FT", FormattedDate: time.Now().Format("02.01.2006")}}

	const start = "Updating user scores - START"
	emailArgs1 := getEmailArgs(start)
	const end = "Updating user scores - END"
	emailArgs2 := getEmailArgs(end)

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs1).Return(nil)
	emailer.On("Send", emailArgs2).Return(nil)
	sms.On("Send", start, MOBILE_NUMBER).Return(nil)
	sms.On("Send", end, MOBILE_NUMBER).Return(nil)
	http.On("Put", SCORE_UPDATE_URL).Return(&Response{}, nil)
	redis.On("SetScoresUpdated").Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs1)
	emailer.AssertCalled(t, "Send", emailArgs2)
	sms.AssertCalled(t, "Send", start, MOBILE_NUMBER)
	sms.AssertCalled(t, "Send", end, MOBILE_NUMBER)
	http.AssertCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertCalled(t, "SetScoresUpdated")
	assert.Equal(t, nil, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorIfThereAreNoMatchesToday(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "FT", FormattedDate: time.Now().AddDate(0, 0, -1).Format("02.01.2006")}}
	emailArgs := getEmailArgs(ErrScoresDoNotNeedUpdating.Error())

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)
	sms.On("Send", ErrScoresDoNotNeedUpdating.Error(), MOBILE_NUMBER).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	sms.AssertCalled(t, "Send", ErrScoresDoNotNeedUpdating.Error(), MOBILE_NUMBER)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresDoNotNeedUpdating, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorAndSendsEmailIfAllGamesHaveNotFinished(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	matchFacts := []MatchFacts{{Status: "88", FormattedDate: time.Now().AddDate(0, 0, -1).Format("02.01.2006")}}
	emailArgs := getEmailArgs(ErrScoresDoNotNeedUpdating.Error())

	api.On("GetTodaysMatches").Return(matchFacts, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)
	sms.On("Send", ErrScoresDoNotNeedUpdating.Error(), MOBILE_NUMBER).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	sms.AssertCalled(t, "Send", ErrScoresDoNotNeedUpdating.Error(), MOBILE_NUMBER)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresDoNotNeedUpdating, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorAndSendsEmailIfThereIsAnApiError(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	emailArgs := getEmailArgs(ErrUpdatingScores.Error())

	api.On("GetTodaysMatches").Return([]MatchFacts{}, ErrUpdatingScores)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs).Return(nil)
	sms.On("Send", ErrUpdatingScores.Error(), MOBILE_NUMBER).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	sms.AssertCalled(t, "Send", ErrUpdatingScores.Error(), MOBILE_NUMBER)
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrUpdatingScores, result)
}

func TestUpdateUserScoresReturnsNotUpdatedErrorIfScoresHaveAlreadyBeenUpdatedToday(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	emailArgs := getEmailArgs(ErrScoresAlreadyUpdated.Error())

	redis.On("GetScoresUpdated").Return(time.Now())
	emailer.On("Send", emailArgs).Return(nil)
	sms.On("Send", ErrScoresAlreadyUpdated.Error(), MOBILE_NUMBER).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs)
	sms.AssertCalled(t, "Send", ErrScoresAlreadyUpdated.Error(), MOBILE_NUMBER)
	api.AssertNotCalled(t, "GetTodaysMatches")
	http.AssertNotCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrScoresAlreadyUpdated, result)
}

func TestUpdateUserScoresReturnsErrorIfScoresAreNotUpdatedSuccessfully(t *testing.T) {
	api, redis, emailer, http, sms, scoreUpdateService := setup()

	emailArgs1 := getEmailArgs("Updating user scores - START")
	emailArgs2 := getEmailArgs(ErrUpdatingScores.Error())

	api.On("GetTodaysMatches").Return([]MatchFacts{}, nil)
	redis.On("GetScoresUpdated").Return(time.Now().AddDate(0, 0, -1))
	emailer.On("Send", emailArgs1).Return(nil)
	emailer.On("Send", emailArgs2).Return(nil)
	http.On("Put", SCORE_UPDATE_URL).Return(nil, errors.New(""))
	emailer.On("Send", emailArgs1).Return(nil)
	sms.On("Send", "Updating user scores - START", MOBILE_NUMBER).Return(nil)
	sms.On("Send", ErrUpdatingScores.Error(), MOBILE_NUMBER).Return(nil)

	result := scoreUpdateService.UpdateUserScores()

	api.AssertCalled(t, "GetTodaysMatches")
	redis.AssertCalled(t, "GetScoresUpdated")
	emailer.AssertCalled(t, "Send", emailArgs1)
	sms.AssertCalled(t, "Send", "Updating user scores - START", MOBILE_NUMBER)
	sms.AssertCalled(t, "Send", ErrUpdatingScores.Error(), MOBILE_NUMBER)
	http.AssertCalled(t, "Put", SCORE_UPDATE_URL)
	redis.AssertNotCalled(t, "SetScoresUpdated")
	assert.Equal(t, ErrUpdatingScores, result)
}

func setup() (*mock.ApiRequester, *mock2.RedisRepository, *mock3.Emailer, *mock4.HttpWrapper, *mock5.SmsSender, ScoreUpdateService) {
	apiMock := new(mock.ApiRequester)
	redisMock := new(mock2.RedisRepository)
	emailerMock := new(mock3.Emailer)
	httpMock := new(mock4.HttpWrapper)
	smsMock := new(mock5.SmsSender)

	scoreUpdateService := ScoreUpdateService{
		api:     apiMock,
		redis:   redisMock,
		emailer: emailerMock,
		http:    httpMock,
		sms:     smsMock,
	}

	redisMock.On("Close")

	os.Setenv("PHONE_NUMBER", MOBILE_NUMBER)

	return apiMock, redisMock, emailerMock, httpMock, smsMock, scoreUpdateService
}
