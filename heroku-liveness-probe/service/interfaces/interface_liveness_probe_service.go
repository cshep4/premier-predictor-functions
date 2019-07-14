package interfaces

type LivenessProbeService interface {
	ServiceName() string
	Check() error
}
