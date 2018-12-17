package interfaces

type LiveMatchCheckService interface {
	ServiceName() string
	UpdateLiveMatches() bool
}

