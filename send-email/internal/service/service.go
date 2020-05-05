package email

import (
	"github.com/cshep4/premier-predictor-functions/common/domain"
	iface "github.com/cshep4/premier-predictor-functions/common/email/interfaces"
	"github.com/cshep4/premier-predictor-functions/common/redis/interfaces"
)

type Service interface {
	SendEmail(req SendEmailRequest) error
}

type service struct {
	redis   interfaces.RedisRepository
	emailer iface.Emailer
}

func NewService(redis interfaces.RedisRepository, emailer iface.Emailer) *service {
	return &service{redis: redis, emailer: emailer}
}

func (s *service) SendEmail(req SendEmailRequest) error {
	if req.IdempotencyKey == "" {
		return nil
	}

	ok, err := s.redis.CheckIdempotency(req.IdempotencyKey)
	if ok && err == nil {
		return nil
	}

	emailArgs := &domain.EmailArgs{
		Sender:    req.Sender,
		Recipient: req.Recipient,
		From:      req.SenderEmail,
		To:        req.RecipientEmail,
		Subject:   req.Subject,
		Content:   req.Content,
	}

	err = s.emailer.Send(emailArgs)
	if err != nil {
		return err
	}

	s.redis.SetIdempotency(req.IdempotencyKey)

	return nil
}
