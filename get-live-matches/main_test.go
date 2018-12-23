package main

import (
	"encoding/json"
	"github.com/stretchr/testify/assert"
	"log"
	. "premier-predictor-functions/common/domain"
	"premier-predictor-functions/get-live-matches/factory"
	. "premier-predictor-functions/get-live-matches/factory/mock"
	"testing"
)

func TestResponseIsOkWhenLiveMatchesAreRetrievedSuccessfully(t *testing.T) {
	liveMatches := []LiveMatch{{}, {}}
	liveMatchServiceFactory = MockLiveMatchServiceFactory(liveMatches)

	resp, err := Handler()

	expectedResponse, _ := json.Marshal(liveMatches)
	expectedBody := string(expectedResponse)

	assert.Equal(t, resp.StatusCode, 200)
	assert.Equal(t, resp.Body, expectedBody)
	assert.Equal(t, err, nil)
}

func TestResponseIsNotFoundWhenThereAreNoLiveMatchesPlaying(t *testing.T) {
	liveMatchServiceFactory = MockLiveMatchServiceFactory([]LiveMatch{})

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 404)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestIntegration(t *testing.T) {
	liveMatchServiceFactory = factory.LiveMatchServiceFactory{}

	resp, err := Handler()

	log.Println(resp)

	assert.Equal(t, err, nil)
}