package api

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	. "net/http"
	"os"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "github.com/cshep4/premier-predictor-functions/common/http"
	"github.com/cshep4/premier-predictor-functions/common/http/interfaces"
	"time"
)

type ApiRequester struct {
	http   interfaces.HttpWrapper
	apiUrl string
	apiKey string
}

const fromDate string = "from_date="
const toDate string = "to_date="
const compId string = "comp_id=1204"
const authorisation string = "Authorization="

var errIncorrectResponse = errors.New("Response not in correct formt")
var errRequestUnsuccessful = errors.New("API request not successful")
var ErrNotFound = errors.New("No matches found")

func InjectApiRequester() ApiRequester {
	return ApiRequester{
		http:   HttpWrapper{},
		apiUrl: os.Getenv("API_URL"),
		apiKey: os.Getenv("API_KEY"),
	}
}

func (a ApiRequester) GetTodaysMatches() ([]MatchFacts, error) {
	var matchFacts []MatchFacts

	url := url(a.apiUrl, a.apiKey)
	resp, err := a.http.Get(url)

	if err == nil && resp.StatusCode == StatusNotFound {
		return []MatchFacts{}, ErrNotFound
	}

	if resp.StatusCode != StatusOK || err != nil {
		return []MatchFacts{}, errRequestUnsuccessful
	}

	defer resp.Body.Close()

	body, _ := ioutil.ReadAll(resp.Body)
	marshalErr := json.Unmarshal(body, &matchFacts)

	if marshalErr != nil {
		return []MatchFacts{}, errIncorrectResponse
	}

	return matchFacts, nil
}

func url(apiUrl string, apiKey string) string {
	yesterday := time.Now().UTC().AddDate(0, 0, -1).Local().Format("2006-01-02")
	tomorrow := time.Now().UTC().AddDate(0, 0, 1).Local().Format("2006-01-02")

	return apiUrl + "?" + fromDate + yesterday + "&" + toDate + tomorrow + "&" + compId + "&" + authorisation + apiKey
}