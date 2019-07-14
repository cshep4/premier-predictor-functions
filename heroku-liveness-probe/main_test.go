package main

import (
	"errors"
	"github.com/stretchr/testify/assert"
	"log"
	"premier-predictor-functions/heroku-liveness-probe/factory"
	. "premier-predictor-functions/heroku-liveness-probe/factory/mock"
	"testing"
)

func TestLivenessProbe(t *testing.T) {
	t.Run("Response is OK when there is no error", func(t *testing.T) {
		livenessProbeServiceFactory = MockLivenessProbeServiceFactory(nil)

		resp, err := Handler()

		assert.Equal(t, 200, resp.StatusCode)
		assert.Equal(t, "", resp.Body)
		assert.NoError(t, err)
	})

	t.Run("Response is Internal Server Error when there is an error", func(t *testing.T) {
		livenessProbeServiceFactory = MockLivenessProbeServiceFactory(errors.New(""))

		resp, err := Handler()

		assert.Equal(t, 500, resp.StatusCode)
		assert.Equal(t, "", resp.Body)
		assert.NoError(t, err)
	})

	t.Run("Integration test", func(t *testing.T) {
		livenessProbeServiceFactory = factory.LivenessProbeServiceFactory{}

		resp, err := Handler()

		log.Println(resp)

		assert.Equal(t, err, nil)
	})
}
