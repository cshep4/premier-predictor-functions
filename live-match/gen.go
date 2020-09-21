package main

//go:generate mockgen -destination=./internal/mocks/live/live_mock.gen.go -package=live_mocks github.com/cshep4/premier-predictor-functions/live-match/internal/handler/aws LiveMatchService
//go:generate mockgen -destination=./internal/mocks/storage/repo_mock.gen.go -package=storage_mocks github.com/cshep4/premier-predictor-functions/live-match/internal/live Store
//go:generate mockgen -destination=./internal/mocks/api/api_mock.gen.go -package=api_mocks github.com/cshep4/premier-predictor-functions/live-match/internal/live Retriever
