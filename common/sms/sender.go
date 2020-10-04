package sms

import (
	"fmt"

	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/service/sns"
)

type (
	sender struct {
		sns *sns.SNS
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(sns *sns.SNS) (*sender, error) {
	if sns == nil {
		return nil, InvalidParameterError{Parameter: "sns"}
	}
	return &sender{
		sns: sns,
	}, nil
}

func (s *sender) Send(message, phoneNumber string) error {
	_, err := s.sns.Publish(&sns.PublishInput{
		Message:     aws.String(message),
		PhoneNumber: aws.String(phoneNumber),
		MessageAttributes: map[string]*sns.MessageAttributeValue{
			"AWS.SNS.SMS.SenderID": {
				DataType:    aws.String("String"),
				StringValue: aws.String("PremPred"),
			},
			"DefaultSMSType": {
				DataType:    aws.String("String"),
				StringValue: aws.String("Transactional"),
			},
		},
	})
	if err != nil {
		return fmt.Errorf("publish: %w", err)
	}
	return err
}
