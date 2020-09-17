_build-http:
    GOOS=linux go build -o bin/http/main ./pkg/http

_build-sqs:
    GOOS=linux go build -o bin/sqs/main ./pkg/sqs

build:
	GOOS=linux go build -o bin/http/main ./pkg/http
	GOOS=linux go build -o bin/sqs/main ./pkg/sqs