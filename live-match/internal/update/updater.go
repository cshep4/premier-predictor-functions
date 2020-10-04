package update

import (
	"context"
	"fmt"

	"github.com/aws/aws-sdk-go/service/sfn"
)

type (
	matchUpdater struct {
		sfn          sfn.SFN
		stateMachine string
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(sfn *sfn.SFN, stateMachine string) (*matchUpdater, error) {
	switch {
	case sfn == nil:
		return nil, InvalidParameterError{Parameter: "sfn"}
	case stateMachine == "":
		return nil, InvalidParameterError{Parameter: "stateMachineArn"}
	}

	return &matchUpdater{
		sfn:          *sfn,
		stateMachine: stateMachine,
	}, nil
}

func (m *matchUpdater) IsRunning(ctx context.Context) (bool, error) {
	res, err := m.sfn.ListExecutionsWithContext(ctx, &sfn.ListExecutionsInput{
		StateMachineArn: &m.stateMachine,
	})
	if err != nil {
		return false, fmt.Errorf("list_executions: %w", err)
	}

	for _, e := range res.Executions {
		if *e.Status == sfn.ExecutionStatusRunning {
			return true, nil
		}
	}

	return false, nil
}

func (m *matchUpdater) Start(ctx context.Context) error {
	_, err := m.sfn.StartExecutionWithContext(ctx, &sfn.StartExecutionInput{
		StateMachineArn: &m.stateMachine,
	})
	if err != nil {
		return fmt.Errorf("start_execution: %w", err)
	}

	return nil
}
