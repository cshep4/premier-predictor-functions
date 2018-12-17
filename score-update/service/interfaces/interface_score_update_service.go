package interfaces

type ScoreUpdateService interface {
	ServiceName() string
	UpdateUserScores() error
}
