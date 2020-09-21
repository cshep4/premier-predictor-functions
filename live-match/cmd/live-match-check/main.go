package main

import (
	"context"
	"fmt"

	"github.com/cshep4/lambda-go/lambda"
	"github.com/cshep4/lambda-go/log"
	"github.com/cshep4/premier-predictor-functions/common/api"
	"github.com/cshep4/premier-predictor-functions/common/redis"

	"github.com/cshep4/premier-predictor-functions/live-match/internal/handler/aws"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/live"
)

const (
	logLevel     = "info"
	serviceName  = "premier-predictor"
	functionName = "live-match-check"
)

var (
	cfg = lambda.FunctionConfig{
		LogLevel:     logLevel,
		ServiceName:  serviceName,
		FunctionName: functionName,
		Setup:        setup,
		Initialised:  func() bool { return handler.Service != nil },
	}

	handler aws.Handler

	runner = lambda.New(
		handler.CheckLiveMatches,
		lambda.WithPreExecute(log.Middleware(logLevel, serviceName, functionName)),
	)
)

func main() {
	runner.Start(cfg)
}

func setup(context.Context) error {
	service, err := live.New(api.InjectApiRequester(), redis.InjectRedisRepository())
	if err != nil {
		return fmt.Errorf("new_live_match_service: %w", err)
	}

	handler.Service = service

	return nil
}
