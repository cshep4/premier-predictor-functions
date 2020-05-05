package main

import (
	"context"
	"encoding/json"
	"errors"
	"github.com/aws/aws-lambda-go/events"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/mocks/email"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/service"
	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"testing"
)

func Test_handler(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	serviceMock := email_mocks.NewMockService(ctrl)
	service = serviceMock

	req := email.SendEmailRequest{
		IdempotencyKey: "1",
	}
	b, err := json.Marshal(req)
	require.NoError(t, err)

	event := events.SQSEvent{
		Records: []events.SQSMessage{
			{
				Body: string(b),
			},
		},
	}

	t.Run("returns error when there are no messages on queue", func(t *testing.T) {
		err := handler(context.Background(), events.SQSEvent{})
		require.Error(t, err)

		assert.Equal(t, "no SQS message passed to function", err.Error())
	})

	t.Run("does not send email when message body is invalid format", func(t *testing.T) {
		event := events.SQSEvent{
			Records: []events.SQSMessage{
				{
					Body: "Invalid body",
				},
			},
		}

		err := handler(context.Background(), event)
		require.NoError(t, err)
	})

	t.Run("returns error when there is an error sending email", func(t *testing.T) {
		testErr := errors.New("error")

		serviceMock.EXPECT().SendEmail(req).Return(testErr)

		err := handler(context.Background(), event)
		require.Error(t, err)

		assert.Equal(t, testErr, err)
	})

	t.Run("returns nil when there all emails sent successfully", func(t *testing.T) {
		serviceMock.EXPECT().SendEmail(req).Return(nil)

		err := handler(context.Background(), event)
		require.NoError(t, err)
	})
}
