package sms

import (
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/aws/credentials"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/sns"
	"os"
)

type Sender struct {
}

func InjectSender() Sender {
	return Sender{}
}

func (s Sender) Send(message, phoneNumber string) error {
	sess := session.Must(session.NewSession(&aws.Config{
		Region:      aws.String("us-east-1"),
		Credentials: credentials.NewStaticCredentials(os.Getenv("DYNAMO_DB_ACCESS_KEY"), os.Getenv("DYNAMO_DB_SECRET_KEY"), ""),
	}))

	svc := sns.New(sess)

	m := map[string]*sns.MessageAttributeValue{
		"AWS.SNS.SMS.SenderID": {
			DataType:    aws.String("String"),
			StringValue: aws.String("PremPred"),
		},
		"DefaultSMSType": {
			DataType:    aws.String("String"),
			StringValue: aws.String("Transactional"),
		},
	}

	params := &sns.PublishInput{
		Message:           aws.String(message),
		PhoneNumber:       aws.String(phoneNumber),
		MessageAttributes: m,
	}

	_, err := svc.Publish(params)

	return err
}
