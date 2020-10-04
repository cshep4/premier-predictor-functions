package sms

import (
	"fmt"
	"os"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/credentials"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/sns"
)

func NewSession() (*sns.SNS, error) {
	var (
		region    string
		accessKey string
		secretKey string
	)
	for k, v := range map[string]*string{
		"AWS_REGION":           &region,
		"DYNAMO_DB_ACCESS_KEY": &accessKey,
		"DYNAMO_DB_SECRET_KEY": &secretKey,
	} {
		var ok bool
		if *v, ok = os.LookupEnv(k); !ok {
			return nil, fmt.Errorf("missing environment variable %s", k)
		}
	}

	sess, err := session.NewSession(&aws.Config{
		Region:      aws.String(region),
		Credentials: credentials.NewStaticCredentials(accessKey, secretKey, ""),
	})
	if err != nil {
		return nil, fmt.Errorf("new_session: %w", err)
	}

	return sns.New(sess), nil
}
