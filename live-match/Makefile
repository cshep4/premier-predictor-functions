
default: build

build:
	GOOS=linux go build -ldflags "-s -w" -o bin/live-match-check ./cmd/live-match-check
	GOOS=linux go build -ldflags "-s -w" -o bin/get-live-matches ./cmd/get-live-matches