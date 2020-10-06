package main

import (
	"context"
	"errors"
	"fmt"
	"os"

	awsconfig "github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/session"
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

func setup(ctx context.Context) error {
	region, ok := os.LookupEnv("REGION")
	if !ok {
		return errors.New("region not set")
	}

	stateMachine, ok := os.LookupEnv("STATE_MACHINE_ARN")
	if !ok {
		return errors.New("region not set")
	}

	sess, err := session.NewSession(&awsconfig.Config{
		Region: &region,
	})
	if err != nil {
		return fmt.Errorf("new_session: %w", err)
	}

	updater, err := update.New(sfn.New(sess), stateMachine)
	if err != nil {
		return fmt.Errorf("new_match_updater: %w", err)
	}

	handler.Service, err = live.New(api.InjectApiRequester(), redis.InjectRedisRepository(), updater)
	if err != nil {
		return fmt.Errorf("new_live_match_service: %w", err)
	}

	return nil
}
