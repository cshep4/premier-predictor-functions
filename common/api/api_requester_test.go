package api

import (
	"bytes"
	"encoding/json"
	"errors"
	"github.com/stretchr/testify/assert"
	"io/ioutil"
	. "net/http"
	. "premier-predictor-functions/common/domain"
	"premier-predictor-functions/common/http/mock"
	"testing"
)

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnAListOfMatches(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	matchFacts := []MatchFacts{{}, {}}
	resp := createResponse(matchFacts)

	mockHttp.On("Get", url).Return(&resp, nil)

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, matchFacts, result)
	assert.Nil(t, err)
}

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnEmptyListIfNoMatches(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	resp := createResponse([]MatchFacts{})

	mockHttp.On("Get", url).Return(&resp, nil)

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, []MatchFacts{}, result)
	assert.Nil(t, err)
}

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnEmptyListWithErrorIfResponseIsNotCorrectFormat(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	matchFacts := "NotMatchFactsObjects"
	resp := createResponse(matchFacts)

	mockHttp.On("Get", url).Return(&resp, nil)

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, []MatchFacts{}, result)
	assert.Equal(t, errIncorrectResponse, err)
}

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnEmptyListWithErrorIfRequestIsNotSuccess(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	matchFacts := []MatchFacts{{}, {}}
	resp := createResponse(matchFacts)
	resp.StatusCode = StatusTooManyRequests

	mockHttp.On("Get", url).Return(&resp, nil)

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, []MatchFacts{}, result)
	assert.Equal(t, errRequestUnsuccessful, err)
}

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnEmptyListWithErrorIfNoMatchesFound(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	matchFacts := []MatchFacts{{}, {}}
	resp := createResponse(matchFacts)
	resp.StatusCode = StatusNotFound

	mockHttp.On("Get", url).Return(&resp, nil)

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, []MatchFacts{}, result)
	assert.Equal(t, ErrNotFound, err)
}

func TestGetTodaysMatchesWillMakeARequestToApiAndReturnEmptyListWithErrorIfResponseStatusCodeIsNot200(t *testing.T) {
	mockHttp, apiRequester, url := setup()

	matchFacts := []MatchFacts{{}, {}}
	resp := createResponse(matchFacts)

	mockHttp.On("Get", url).Return(&resp, errors.New(""))

	result, err := apiRequester.GetTodaysMatches()

	assert.Equal(t, []MatchFacts{}, result)
	assert.Equal(t, errRequestUnsuccessful, err)
}

func setup() (*mock.HttpWrapper, ApiRequester, string) {
	apiUrl := "testUrl"
	apiKey := "testKey"
	mockHttp := new(mock.HttpWrapper)
	apiRequester := ApiRequester{
		http:   mockHttp,
		apiUrl: apiUrl,
		apiKey: apiKey,
	}
	url := url(apiUrl, apiKey)
	return mockHttp, apiRequester, url
}

func createResponse(jsonBody interface{}) Response {
	body, _ := json.Marshal(jsonBody)
	resp := Response{
		StatusCode: StatusOK,
		Body: ioutil.NopCloser(bytes.NewBuffer(body)),
	}
	return resp
}
