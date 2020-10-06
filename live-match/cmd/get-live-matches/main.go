package main

import (
	"context"
	"fmt"

	"github.com/aws/aws-sdk-go/service/sfn"
	"github.com/cshep4/lambda-go/lambda"
	"github.com/cshep4/lambda-go/log"
	"github.com/cshep4/premier-predictor-functions/common/api"
	"github.com/cshep4/premier-predictor-functions/common/redis"

	"github.com/cshep4/premier-predictor-functions/live-match/internal/handler/aws"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/live"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/update"
)

const (
	logLevel     = "info"
	serviceName  = "premier-predictor"
	functionName = "get-live-matches"
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
		handler.GetLiveMatches,
		lambda.WithPreExecute(log.Middleware(logLevel, serviceName, functionName)),
	)
)

func main() {
	runner.Start(cfg)
}

func setup(ctx context.Context) error {
	updater, err := update.New(&sfn.SFN{}, "state machine")
	if err != nil {
		return fmt.Errorf("new_match_updater: %w", err)
	}

	handler.Service, err = live.New(api.InjectApiRequester(), redis.InjectRedisRepository(), updater)
	if err != nil {
		return fmt.Errorf("new_live_match_service: %w", err)
	}

	return nil
}
