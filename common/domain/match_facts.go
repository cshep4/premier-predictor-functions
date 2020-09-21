package domain

import (
	"fmt"
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

func (m MatchFacts) IsToday() (bool, error) {
	dateTime := fmt.Sprintf("%sT%s", m.FormattedDate, m.Time)
	matchTime, err := time.Parse("02.01.2006T15:04", dateTime)
	if err != nil {
		return false, err
	}

	y1, m1, d1 := matchTime.Date()
	y2, m2, d2 := time.Now().Date()

	return y1 == y2 && m1 == m2 && d1 == d2, nil
}

func (m MatchFacts) IsAboutToStart() (bool, error) {
	layout := "02.01.2006T15:04"
	str := m.FormattedDate + "T" + m.Time

	kickOffTime, err := time.Parse(layout, str)
	if err != nil {
		return false, fmt.Errorf("parse: %w", err)
	}
	thirtyMinutesTime := time.Now().
		UTC().
		Add(time.Minute * time.Duration(30))

	return m.Status != "FT" &&
		m.Status != "Postp." &&
		m.Status != "Cancl." &&
		m.Status != "Awarded" &&
		m.Status != "Aban." &&
		(kickOffTime.Equal(thirtyMinutesTime) || kickOffTime.Before(thirtyMinutesTime)), nil
}

func (m MatchFacts) ShouldBeUpdated() (bool, error) {
	today, err := m.IsToday()
	if err != nil {
		return false, fmt.Errorf("is_today: %w", err)
	}

	aboutToStart, err := m.IsAboutToStart()
	if err != nil {
		return false, fmt.Errorf("is_about_to_start: %w", err)
	}

	return today && (m.IsPlaying() || aboutToStart), nil
}
