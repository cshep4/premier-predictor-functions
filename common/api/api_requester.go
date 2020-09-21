package api

import (
	"encoding/json"
	"errors"
	"fmt"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "github.com/cshep4/premier-predictor-functions/common/http"
	"github.com/cshep4/premier-predictor-functions/common/http/interfaces"
	"io/ioutil"
	. "net/http"
	"os"
	"time"
)

type ApiRequester struct {
	http   interfaces.HttpWrapper
	apiUrl string
	apiKey string
}

const fromDate string = "fromDate="
const toDate string = "toDate="
const compId string = "comp_id=1204"
const authorisation string = "Authorization="

var errIncorrectResponse = errors.New("response not in correct format")
var errRequestUnsuccessful = errors.New("api request not successful")
var ErrNotFound = errors.New("no matches found")

func InjectApiRequester() ApiRequester {
	return ApiRequester{
		http:   HttpWrapper{},
		apiUrl: os.Getenv("API_URL"),
		apiKey: os.Getenv("API_KEY"),
	}
}

func (a ApiRequester) GetTodaysMatches() ([]MatchFacts, error) {
	var matchFacts []MatchFacts

	url := url(a.apiUrl)
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

func url(apiUrl string) string {
	yesterday := time.Now().UTC().AddDate(0, 0, -1).Local().Format("2006-01-02")
	tomorrow := time.Now().UTC().AddDate(0, 0, 1).Local().Format("2006-01-02")

	return fmt.Sprintf("%sepl/events?fromDate=%s&toDate=%s", apiUrl, yesterday, tomorrow)
}
