package aws_test

import (
	"context"
	"errors"
	"testing"

	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	"github.com/cshep4/premier-predictor-functions/live-match/internal/handler/aws"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/mocks/live"
)

type testError string

func (e testError) Error() string {
	return string(e)
}

func TestHandler_UpdateLiveMatches_Error(t *testing.T) {
	const testErr = testError("error")

	type args struct {
		ctx         context.Context
		updateErr   error
		expectedErr error
	}
	tests := []struct {
		name string
		args args
	}{
		{
			name: "error updating live matches",
			args: args{
				ctx:         context.Background(),
				updateErr:   testErr,
				expectedErr: testErr,
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctrl := gomock.NewController(t)
			defer ctrl.Finish()

			lmu := live_mocks.NewMockLiveMatchService(ctrl)

			handler := aws.Handler{
				Service: lmu,
			}

			lmu.EXPECT().CheckLiveMatches(tt.args.ctx).Return(tt.args.updateErr)

			err := handler.CheckLiveMatches(tt.args.ctx)
			require.Error(t, err)

			assert.True(t, errors.Is(err, tt.args.expectedErr))
		})
	}
}

func TestHandler_UpdateLiveMatches_Success(t *testing.T) {
	type args struct {
		ctx context.Context
	}
	tests := []struct {
		name string
		args args
	}{
		{
			name: "successfully updates live matches",
			args: args{
				ctx: context.Background(),
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctrl := gomock.NewController(t)
			defer ctrl.Finish()

			lmu := live_mocks.NewMockLiveMatchService(ctrl)

			handler := aws.Handler{
				Service: lmu,
			}

			lmu.EXPECT().CheckLiveMatches(tt.args.ctx).Return(nil)

			err := handler.CheckLiveMatches(tt.args.ctx)
			require.NoError(t, err)
		})
	}
}
