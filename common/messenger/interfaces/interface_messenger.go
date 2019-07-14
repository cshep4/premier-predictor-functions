package interfaces

type Messenger interface {
	Send(message string) error
}
