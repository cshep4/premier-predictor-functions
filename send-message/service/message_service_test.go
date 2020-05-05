package service

import (
	"errors"
	"fmt"
	"github.com/stretchr/testify/assert"
	"os"
	"github.com/cshep4/premier-predictor-functions/common/http/mock"
	"github.com/cshep4/premier-predictor-functions/send-message/domain"
	"testing"
)

const (
	psid        = "testId"
	accessToken = "token"
)

func TestSend(t *testing.T) {
	os.Setenv("PSID", psid)
	os.Setenv("ACCESS_TOKEN", accessToken)

	url := fmt.Sprintf(domain.URL, accessToken)

	message := "test"

	body := domain.SendApiBody{
		MessagingType: "UPDATE",
		Recipient: domain.Recipient{
			Id: psid,
		},
		Message: domain.Message{
			Text: message,
		},
	}

	http := &mock.HttpWrapper{}
	messageService := MessageService{http: http}

	t.Run("Will call Facebook SendAPI with no error", func(t *testing.T) {
		http.On("Post", url, body).Once().Return(nil, nil)

		result := messageService.Send(message)

		assert.NoError(t, result)
		http.AssertExpectations(t)
	})

	t.Run("Will return error if there is a problem with Facebook SendAPI", func(t *testing.T) {
		http.On("Post", url, body).Once().Return(nil, errors.New(""))

		result := messageService.Send(message)

		assert.Error(t, result)
		http.AssertExpectations(t)
	})
}
