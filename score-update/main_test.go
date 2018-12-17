package main

import (
	"github.com/stretchr/testify/assert"
	. "premier-predictor-functions/score-update/constant"
	"premier-predictor-functions/score-update/factory"
	. "premier-predictor-functions/score-update/factory/mock"
	"testing"
)

func TestResponseIsOkWhenScoresAreUpdatedSuccessfully(t *testing.T) {
	scoreUpdateServiceFactory = MockScoreUpdateServiceFactory(nil)
	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 200)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestResponseIsNotModifiedWhenScoresHaveAlreadyBeenModified(t *testing.T) {
	scoreUpdateServiceFactory = MockScoreUpdateServiceFactory(ErrScoresAlreadyUpdated)

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 304)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestResponseIsInternalServerErrorWhenScoresAreNotUpdatedSuccessfully(t *testing.T) {
	scoreUpdateServiceFactory = MockScoreUpdateServiceFactory(ErrUpdatingScores)

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 500)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestIntegrationResponseIsOk(t *testing.T) {
	scoreUpdateServiceFactory = factory.ScoreUpdateServiceFactory{}

	resp, err := Handler()

	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}