package messenger

import (
	"os"
	"github.com/cshep4/premier-predictor-functions/common/http"
	"github.com/cshep4/premier-predictor-functions/common/http/interfaces"
)

type Messenger struct {
	http interfaces.HttpWrapper
}

func InjectMessenger() Messenger {
	return Messenger{http: http.InjectHttpWrapper()}
}

func (m Messenger) Send(message string) error {
	url := os.Getenv("MESSENGER_CLIENT_URL")

	body := struct {
		Text string `json:"text,omitempty"`
	}{
		Text: message,
	}

	_, err := m.http.Post(url, body)

	return err
}
