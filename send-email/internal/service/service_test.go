package email_test

import (
	"errors"
	"github.com/cshep4/premier-predictor-functions/common/domain"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/mocks/email"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/mocks/storage"
	"github.com/cshep4/premier-predictor-functions/send-email/internal/service"
	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"
	"testing"
)

func TestService_SendEmail(t *testing.T) {
	ctrl := gomock.NewController(t)
	defer ctrl.Finish()

	redisMock := storage_mocks.NewMockRedisRepository(ctrl)
	emailMock := email_mocks.NewMockEmailer(ctrl)

	service := email.NewService(redisMock, emailMock)

	const (
		sender         = "sender"
		recipient      = "recipient"
		senderEmail    = "senderEmail"
		recipientEmail = "recipientEmail"
		subject        = "subject"
		content        = "content"
		idempotencyKey = "key"
	)

	req := email.SendEmailRequest{
		Sender:         sender,
		Recipient:      recipient,
		SenderEmail:    senderEmail,
		RecipientEmail: recipientEmail,
		Subject:        subject,
		Content:        content,
		IdempotencyKey: idempotencyKey,
	}

	emailArgs := &domain.EmailArgs{
		Sender:    sender,
		Recipient: recipient,
		From:      senderEmail,
		To:        recipientEmail,
		Subject:   subject,
		Content:   content,
	}

	t.Run("return nil when idempotency key is empty", func(t *testing.T) {
		err := service.SendEmail(email.SendEmailRequest{})
		require.NoError(t, err)
	})

	t.Run("return nil when idempotency key already exists", func(t *testing.T) {
		redisMock.EXPECT().CheckIdempotency(idempotencyKey).Return(true, nil)

		err := service.SendEmail(req)
		require.NoError(t, err)
	})

	t.Run("return error when email cannot be sent", func(t *testing.T) {
		testErr := errors.New("error")

		redisMock.EXPECT().CheckIdempotency(idempotencyKey).Return(false, errors.New("error"))
		emailMock.EXPECT().Send(emailArgs).Return(testErr)

		err := service.SendEmail(req)
		require.Error(t, err)

		assert.Equal(t, testErr, err)
	})

	t.Run("store idempotency key and return nil when email sent successfully", func(t *testing.T) {
		redisMock.EXPECT().CheckIdempotency(idempotencyKey).Return(false, nil)
		emailMock.EXPECT().Send(emailArgs).Return(nil)

		redisMock.EXPECT().SetIdempotency(idempotencyKey).Return(nil)

		err := service.SendEmail(req)
		require.NoError(t, err)
	})
}
