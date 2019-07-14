package service

import (
	. "github.com/ahl5esoft/golang-underscore"
	"log"
	. "premier-predictor-functions/common/api"
	interfaces2 "premier-predictor-functions/common/api/interfaces"
	. "premier-predictor-functions/common/domain"
	. "premier-predictor-functions/common/email"
	interfaces3 "premier-predictor-functions/common/email/interfaces"
	. "premier-predictor-functions/common/http"
	interfaces4 "premier-predictor-functions/common/http/interfaces"
	. "premier-predictor-functions/common/messenger"
	interfaces5 "premier-predictor-functions/common/messenger/interfaces"
	. "premier-predictor-functions/common/redis"
	"premier-predictor-functions/common/redis/interfaces"
	"premier-predictor-functions/common/util"
	. "premier-predictor-functions/score-update/constant"
	"time"
)

type ScoreUpdateService struct {
	redis     interfaces.RedisRepository
	api       interfaces2.ApiRequester
	emailer   interfaces3.Emailer
	http      interfaces4.HttpWrapper
	messenger interfaces5.Messenger
}

func InjectScoreUpdateService() ScoreUpdateService {
	return ScoreUpdateService{
		redis:     InjectRedisRepository(),
		api:       InjectApiRequester(),
		emailer:   InjectEmailer(),
		http:      InjectHttpWrapper(),
		messenger: InjectMessenger(),
	}
}

func (s ScoreUpdateService) ServiceName() string {
	return "ScoreUpdate"
}

const UPDATING_SCORES = "Updating user scores"

func (s ScoreUpdateService) UpdateUserScores() error {
	defer s.redis.Close()

	if isToday(s.redis.GetScoresUpdated()) {
		log.Println(ErrScoresAlreadyUpdated.Error())
		s.emailer.Send(getEmailArgs(ErrScoresAlreadyUpdated.Error()))
		s.messenger.Send(ErrScoresAlreadyUpdated.Error())
		return ErrScoresAlreadyUpdated
	}

	matches, err := s.api.GetTodaysMatches()

	if err != nil {
		log.Println(err.Error())
		s.emailer.Send(getEmailArgs(err.Error()))
		s.messenger.Send(err.Error())
		return err
	}

	if Any(matches, isTodayAndNotFinished) || All(matches, isNotToday) {
		log.Println(ErrScoresDoNotNeedUpdating.Error())
		s.emailer.Send(getEmailArgs(ErrScoresDoNotNeedUpdating.Error()))
		s.messenger.Send(ErrScoresDoNotNeedUpdating.Error())
		return ErrScoresDoNotNeedUpdating
	}

	log.Println(UPDATING_SCORES + " - START")
	e := s.emailer.Send(getEmailArgs(UPDATING_SCORES + " - START"))
	s.messenger.Send(UPDATING_SCORES + " - START")
	util.CheckErr(e)

	_, updateErr := s.http.Put(SCORE_UPDATE_URL, nil)

	if updateErr != nil {
		log.Println(ErrUpdatingScores.Error())
		s.emailer.Send(getEmailArgs(ErrUpdatingScores.Error()))
		s.messenger.Send(ErrUpdatingScores.Error())
		return ErrUpdatingScores
	}

	log.Println(UPDATING_SCORES + " - END")
	e = s.emailer.Send(getEmailArgs(UPDATING_SCORES + " - END"))
	s.messenger.Send(UPDATING_SCORES + " - END")
	util.CheckErr(e)

	return s.redis.SetScoresUpdated()
}

var isTodayAndNotFinished = func(m MatchFacts, _ int) bool {
	date, _ := time.Parse("02.01.2006", m.FormattedDate)

	return isToday(date) && m.Status != "FT"
}

var isNotToday = func(m MatchFacts, _ int) bool {
	date, _ := time.Parse("02.01.2006", m.FormattedDate)

	return !isToday(date)
}

func getEmailArgs(subject string) *EmailArgs {
	return &EmailArgs{To: EMAIL_ADDRESS, From: EMAIL_ADDRESS, Sender: SENDER, Recipient: SENDER, Subject: subject, Content: subject}
}

func isToday(date1 time.Time) bool {
	y1, m1, d1 := date1.Date()
	y2, m2, d2 := time.Now().Date()

	return y1 == y2 && m1 == m2 && d1 == d2
}
