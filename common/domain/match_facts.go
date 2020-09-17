package domain

import (
	"strings"
	"time"
)

type MatchFacts struct {
	Id               string      `json:"id,omitempty"`
	CompId           string      `json:"comp_id,omitempty"`
	FormattedDate    string      `json:"formatted_date,omitempty"`
	Season           string      `json:"season,omitempty"`
	Week             string      `json:"week,omitempty"`
	Venue            string      `json:"venue,omitempty"`
	VenueId          string      `json:"venue_id,omitempty"`
	VenueCty         string      `json:"venue_city,omitempty"`
	Status           string      `json:"status,omitempty"`
	Timer            string      `json:"timer,omitempty"`
	Time             string      `json:"time,omitempty"`
	LocalTeamId      string      `json:"localteam_id,omitempty"`
	LocalTeamName    string      `json:"localteam_name,omitempty"`
	LocalTeamScore   string      `json:"localteam_score,omitempty"`
	VisitorTeamId    string      `json:"visitorteam_id,omitempty"`
	VisitorTeamName  string      `json:"visitorteam_name,omitempty"`
	VisitorTeamScore string      `json:"visitorteam_score,omitempty"`
	HtScore          string      `json:"ht_score,omitempty"`
	FtScore          string      `json:"ft_score,omitempty"`
	EtScore          string      `json:"et_score,omitempty"`
	PenaltyLocal     string      `json:"penaltyLocal,omitempty"`
	PenaltyVisitor   string      `json:"penalty_visitor,omitempty"`
	Events           interface{} `json:"events,omitempty"`
}

func (m MatchFacts) ToLiveMatch() LiveMatch {
	return LiveMatch{
		Id:            m.Id,
		HomeTeam:      m.LocalTeamName,
		AwayTeam:      m.VisitorTeamName,
		FormattedDate: m.FormattedDate,
	}
}

func (m MatchFacts) IsPlaying() bool {
	return m.Status != "" &&
		m.Status != "FT" &&
		m.Status != "Postp." &&
		m.Status != "Cancl." &&
		m.Status != "Awarded" &&
		m.Status != "Aban." &&
		!strings.Contains(m.Status, ":")
}

func (m MatchFacts) IsAboutToStart() bool {
	layout := "02.01.2006T15:04"
	str := m.FormattedDate + "T" + m.Time

	kickOffTime, _ := time.Parse(layout, str)
	tenMinutesTime := time.Now().UTC().Add(time.Minute * time.Duration(30))

	return m.Status != "FT" &&
		m.Status != "Postp." &&
		m.Status != "Cancl." &&
		m.Status != "Awarded" &&
		m.Status != "Aban." &&
		(kickOffTime.Equal(tenMinutesTime) || kickOffTime.Before(tenMinutesTime))
}
