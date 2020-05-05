package model

import (
	"fmt"
	"github.com/cshep4/premier-predictor-microservices/proto-gen/model/gen"
	"github.com/golang/protobuf/ptypes"
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
	VenueCity        string      `json:"venue_city,omitempty"`
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
	PenaltyLocal     string      `json:"penalty_local,omitempty"`
	PenaltyVisitor   string      `json:"penalty_visitor,omitempty"`
	Events           []*Event    `json:"events,omitempty"`
	Commentary       *Commentary `json:"commentary,omitempty"`
	MatchDate        time.Time   `json:"matchDate, omitempty"`
}

func (m *MatchFacts) GetDateTime() time.Time {
	layout := "02.01.2006T15:04"
	str := fmt.Sprintf("%sT%s", m.FormattedDate, m.Time)
	t, err := time.Parse(layout, str)

	if err != nil {
		t = time.Date(2020, 6, 10, 12, 0, 0, 0, time.Now().Location())
	}

	return t
}

type Event struct {
	Id       string `json:"id,omitempty"`
	Type     string `json:"type,omitempty"`
	Result   string `json:"result,omitempty"`
	Minute   string `json:"minute,omitempty"`
	ExtraMin string `json:"extra_min,omitempty"`
	Team     string `json:"team,omitempty"`
	Player   string `json:"player,omitempty"`
	PlayerId string `json:"player_id,omitempty"`
	Assist   string `json:"assist,omitempty"`
	AssistId string `json:"assist_id,omitempty"`
}

type Commentary struct {
	MatchId       string         `json:"match_id,omitempty"`
	MatchInfo     []*MatchInfo   `json:"match_info,omitempty"`
	Lineup        *Lineup        `json:"lineup,omitempty"`
	Subs          *Lineup        `json:"subs,omitempty"`
	Substitutions *Substitutions `json:"substitutions,omitempty"`
	Comments      []*Comment     `json:"comments,omitempty"`
	MatchStats    *MatchStats    `json:"match_stats,omitempty"`
	PlayerStats   *PlayerStats   `json:"player_stats,omitempty"`
}

type MatchInfo struct {
	Stadium    string `json:"stadium,omitempty"`
	Attendance string `json:"attendance,omitempty"`
	Referee    string `json:"referee,omitempty"`
}

type Lineup struct {
	LocalTeam   []*Position `json:"localteam,omitempty"`
	VisitorTeam []*Position `json:"visitorteam,omitempty"`
}

type Position struct {
	Id     string `json:"id,omitempty"`
	Number string `json:"number,omitempty"`
	Name   string `json:"name,omitempty"`
	Pos    string `json:"pos,omitempty"`
}

type Substitutions struct {
	LocalTeam   []*Substitution `json:"localteam,omitempty"`
	VisitorTeam []*Substitution `json:"visitorteam,omitempty"`
}

type Substitution struct {
	OffName string `json:"off_name,omitempty"`
	OnName  string `json:"on_name,omitempty"`
	OffId   string `json:"off_id,omitempty"`
	OnId    string `json:"on_id,omitempty"`
	Minute  string `json:"minute,omitempty"`
	TableId string `json:"table_id,omitempty"`
}

type Comment struct {
	Id        string `json:"id,omitempty"`
	Important string `json:"important,omitempty"`
	Goal      string `json:"isgoal,omitempty"`
	Minute    string `json:"minute,omitempty"`
	Comment   string `json:"comment,omitempty"`
}

type MatchStats struct {
	LocalTeam   []*TeamStats `json:"localteam,omitempty"`
	VisitorTeam []*TeamStats `json:"visitorteam,omitempty"`
}

type TeamStats struct {
	ShotsTotal     string `json:"shots_total,omitempty"`
	ShotsOnGoal    string `json:"shots_ongoal,omitempty"`
	Fouls          string `json:"fouls,omitempty"`
	Corners        string `json:"corners,omitempty"`
	Offsides       string `json:"offsides,omitempty"`
	PossessionTime string `json:"possesiontime,omitempty"`
	YellowCards    string `json:"yellowcards,omitempty"`
	RedCards       string `json:"redcards,omitempty"`
	Saves          string `json:"saves,omitempty"`
	TableId        string `json:"table_id,omitempty"`
}

type PlayerStats struct {
	LocalTeam   *Players `json:"localteam,omitempty"`
	VisitorTeam *Players `json:"visitorteam,omitempty"`
}

type Players struct {
	Player []*Player `json:"player,omitempty"`
}

type Player struct {
	Id             string `json:"id,omitempty"`
	Num            string `json:"num,omitempty"`
	Name           string `json:"name,omitempty"`
	Pos            string `json:"pos,omitempty"`
	PosX           string `json:"posx,omitempty"`
	PosY           string `json:"posy,omitempty"`
	ShotsTotal     string `json:"shots_total,omitempty"`
	ShotsOnGoal    string `json:"shots_on_goal,omitempty"`
	Goals          string `json:"goals,omitempty"`
	Assists        string `json:"assists,omitempty"`
	Offsides       string `json:"offsides,omitempty"`
	FoulsDrawn     string `json:"fouls_drawn,omitempty"`
	FoulsCommitted string `json:"fouls_committed,omitempty"`
	Saves          string `json:"saves,omitempty"`
	YellowCards    string `json:"yellowcards,omitempty"`
	RedCards       string `json:"redcards,omitempty"`
	PenScore       string `json:"pen_score,omitempty"`
	PenMiss        string `json:"pen_miss,omitempty"`
}

type MatchFactsSlice []MatchFacts

func (m MatchFactsSlice) Len() int {
	return len(m)
}

func (m MatchFactsSlice) Less(i, j int) bool {
	return m[i].GetDateTime().Before(m[j].GetDateTime())
}

func (m MatchFactsSlice) Swap(i, j int) {
	m[i], m[j] = m[j], m[i]
}
