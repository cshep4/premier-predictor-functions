package domain

const URL = "https://graph.facebook.com/v2.6/me/messages?access_token=%s"

type SendApiBody struct {
	MessagingType string    `json:"messaging_type,omitempty"`
	Recipient     Recipient `json:"recipient,omitempty"`
	Message       Message   `json:"message,omitempty"`
}

type Recipient struct {
	Id string `json:"id,omitempty"`
}

type Message struct {
	Text string `json:"text,omitempty"`
}
