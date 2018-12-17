package domain

type LiveMatch struct {
	Id            string `redis:"id"`
	HomeTeam      string `redis:"hTeam"`
	AwayTeam      string `redis:"aTeam"`
	FormattedDate string `redis:"formattedDate"`
}
