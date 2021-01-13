package main

import (
	"context"
	"fmt"

	"github.com/cshep4/lambda-go/idempotency"
	idempotent "github.com/cshep4/lambda-go/idempotency/middleware"
	"github.com/cshep4/lambda-go/idempotency/middleware/sqs"
	"github.com/cshep4/lambda-go/lambda"
	"github.com/cshep4/lambda-go/log"
	"github.com/cshep4/lambda-go/mongodb"

	"github.com/cshep4/premier-predictor-functions/league-user/internal/handler/aws"
	"github.com/cshep4/premier-predictor-functions/league-user/internal/league"
	"github.com/cshep4/premier-predictor-functions/league-user/internal/store"
)

const (
	logLevel     = "info"
	serviceName  = "premier-predictor"
	functionName = "update-league-user"
)

var (
	cfg = lambda.FunctionConfig{
		LogLevel:     logLevel,
		ServiceName:  serviceName,
		FunctionName: functionName,
		Setup:        setup,
		Initialised:  func() bool { return handler.Service != nil && middleware != nil },
	}

	handler    aws.Handler
	middleware idempotent.Middleware

	runner = lambda.New(
		handler.UpdateLeagueUser,
		lambda.WithPreExecute(log.Middleware(logLevel, serviceName, functionName)),
	)
)

func main() {
	runner.Start(cfg)
}

func setup(ctx context.Context) error {
	mongoClient, err := mongodb.New(ctx)
	if err != nil {
		return fmt.Errorf("initialise_mongo_client: %w", err)
	}

	store, err := store.New(ctx, mongoClient)
	if err != nil {
		return fmt.Errorf("initialise_store: %w", err)
	}

	handler.Service, err = league.New(store)
	if err != nil {
		return fmt.Errorf("initialise_service: %w", err)
	}

	idempotencer, err := idempotency.New(ctx, "update-league-user", mongoClient)
	if err != nil {
		return fmt.Errorf("initialise_idempotencer: %w", err)
	}

	middleware, err = sqs.NewMiddleware(idempotencer)
	if err != nil {
		return fmt.Errorf("initialise_sqs_idempotency_middleware: %w", err)
	}

	runner.Apply(
		lambda.WithPreExecute(middleware.PreExecute),
		lambda.WithPostExecute(middleware.PostExecute),
		lambda.WithErrorHandler(middleware.HandleError),
	)

	return nil
}
