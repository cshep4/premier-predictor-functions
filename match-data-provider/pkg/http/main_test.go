package main

import (
	"encoding/json"
	"errors"
	"github.com/aws/aws-lambda-go/events"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/mocks/email"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/service"
	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"net/http"
	"testing"
)

func Test_handler(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	serviceMock := email_mocks.NewMockService(ctrl)
	service = serviceMock

	emailReq := email.SendEmailRequest{
		IdempotencyKey: "1",
	}
	b, err := json.Marshal(emailReq)
	require.NoError(t, err)

	req := events.APIGatewayProxyRequest{
		Body: string(b),
	}

	t.Run("returns error when request body is invalid format", func(t *testing.T) {
		req := events.APIGatewayProxyRequest{
			Body: "invalid body",
		}

		_, err := handler(req)
		require.Error(t, err)
	})

	t.Run("returns error when there is an error sending email", func(t *testing.T) {
		testErr := errors.New("error")

		serviceMock.EXPECT().SendEmail(emailReq).Return(testErr)

		_, err := handler(req)
		require.Error(t, err)

		assert.Equal(t, testErr, err)
	})

	t.Run("returns nil when there all emails sent successfully", func(t *testing.T) {
		serviceMock.EXPECT().SendEmail(emailReq).Return(nil)

		resp, err := handler(req)
		require.NoError(t, err)

		assert.Equal(t, http.StatusOK, resp.StatusCode)
	})
}
