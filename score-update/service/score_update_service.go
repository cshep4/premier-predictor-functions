package service

import (
	. "github.com/cshep4/premier-predictor-functions/common/api"
	interfaces2 "github.com/cshep4/premier-predictor-functions/common/api/interfaces"
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	. "github.com/cshep4/premier-predictor-functions/common/email"
	interfaces3 "github.com/cshep4/premier-predictor-functions/common/email/interfaces"
	. "github.com/cshep4/premier-predictor-functions/common/http"
	interfaces4 "github.com/cshep4/premier-predictor-functions/common/http/interfaces"
	. "github.com/cshep4/premier-predictor-functions/common/messenger"
	interfaces5 "github.com/cshep4/premier-predictor-functions/common/messenger/interfaces"
	. "github.com/cshep4/premier-predictor-functions/common/redis"
	"github.com/cshep4/premier-predictor-functions/common/redis/interfaces"
	"github.com/cshep4/premier-predictor-functions/common/util"
	. "github.com/cshep4/premier-predictor-functions/score-update/constant"
	"log"
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

	var (
		needUpdating = true
		today        = false
	)
	for _, m := range matches {
		if isTodayAndNotFinished(m) {
			needUpdating = false
			break
		}
		if !isNotToday(m) {
			today = true
		}
	}

	if !needUpdating || !today {
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

func isTodayAndNotFinished(m MatchFacts) bool {
	date, _ := time.Parse("02.01.2006", m.FormattedDate)

	return isToday(date) && m.Status != "FT"
}

func isNotToday(m MatchFacts) bool {
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
