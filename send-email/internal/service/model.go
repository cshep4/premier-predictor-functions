package email

type SendEmailRequest struct {
	Sender         string `json:"sender,omitempty"`
	Recipient      string `json:"recipient,omitempty"`
	SenderEmail    string `json:"senderEmail,omitempty"`
	RecipientEmail string `json:"recipientEmail,omitempty"`
	Subject        string `json:"subject,omitempty"`
	Content        string `json:"content,omitempty"`
	IdempotencyKey string `json:"idempotencyKey,omitempty"`
}
