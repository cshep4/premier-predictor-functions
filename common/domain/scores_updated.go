package domain

import . "time"

type ScoresUpdated struct {
	Id          int  `redis:"id"`
	LastUpdated Time `redis:"lastUpdated"`
}
