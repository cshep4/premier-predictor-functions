package main

//go:generate mockgen -destination=./internal/mocks/email/emailer_mock.gen.go -package=email_mocks github.com/cshep4/premier-predictor-functions/common/email/interfaces Emailer
//go:generate mockgen -destination=./internal/mocks/email/service_mock.gen.go -package=email_mocks github.com/cshep4/premier-predictor-functions/send-email/internal/service Service
//go:generate mockgen -destination=./internal/mocks/storage/repo_mock.gen.go -package=storage_mocks github.com/cshep4/premier-predictor-functions/common/redis/interfaces RedisRepository
