package interfaces

type MessageService interface {
	ServiceName() string
	Send(message string) error
}
