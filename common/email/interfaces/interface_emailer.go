package interfaces

import . "premier-predictor-functions/common/domain"

type Emailer interface {
	Send(args *EmailArgs) error
}