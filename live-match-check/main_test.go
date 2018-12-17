package main

import (
	"github.com/stretchr/testify/assert"
	"premier-predictor-functions/live-match-check/factory"
	. "premier-predictor-functions/live-match-check/factory/mock"
	"testing"
)

func TestResponseIsOkWhenLiveMatchesAreRetrievedSuccessfully(t *testing.T) {
	liveMatchCheckServiceFactory = MockLiveMatchCheckServiceFactory(true)
	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 200)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestResponseIsInternalServerErrorWhenLiveMatchesAreNotRetrievedSuccessfully(t *testing.T) {
	liveMatchCheckServiceFactory = MockLiveMatchCheckServiceFactory(false)

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 500)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestIntegrationResponseIsOk(t *testing.T) {
	liveMatchCheckServiceFactory = factory.LiveMatchCheckServiceFactory{}

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 200)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}