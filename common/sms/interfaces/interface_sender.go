package interfaces

type SmsSender interface {
	Send(message, phoneNumber string) error
}
