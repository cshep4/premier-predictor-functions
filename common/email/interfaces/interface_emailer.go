package interfaces

import . "github.com/cshep4/premier-predictor-functions/common/domain"

type Emailer interface {
	Send(args *EmailArgs) error
}