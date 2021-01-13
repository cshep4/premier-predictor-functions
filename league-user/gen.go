package main

//go:generate mockgen -destination=./internal/mocks/store/store_mock.gen.go -package=store_mocks github.com/cshep4/premier-predictor-functions/league-user/internal/league Store
//go:generate mockgen -destination=./internal/mocks/league/service_mock.gen.go -package=league_mocks github.com/cshep4/premier-predictor-functions/league-user/internal/handler/aws Service
