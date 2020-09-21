package live_test

import (
	"context"
	"testing"
	"time"

	"github.com/cshep4/premier-predictor-functions/common/domain"
	"github.com/golang/mock/gomock"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/require"

	"github.com/cshep4/premier-predictor-functions/live-match/internal/live"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/mocks/api"
	"github.com/cshep4/premier-predictor-functions/live-match/internal/mocks/storage"
)

type testError string

func (e testError) Error() string {
	return string(e)
}

func TestService_UpdateLiveMatches_ShouldBeUpdated_Error(t *testing.T) {
	type args struct {
		ctx        context.Context
		matchFacts []domain.MatchFacts
	}
	tests := []struct {
		name string
		args args
	}{
		{
			name: "error returned when date is invalid",
			args: args{
				ctx: context.Background(),
				matchFacts: []domain.MatchFacts{
					{
						FormattedDate: "invalid",
						Time:          "12:00",
					},
				},
			},
		},
		{
			name: "error returned when time is invalid",
			args: args{
				ctx: context.Background(),
				matchFacts: []domain.MatchFacts{
					{
						FormattedDate: "01.01.2020",
						Time:          "invalid",
					},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctrl := gomock.NewController(t)
			defer ctrl.Finish()

			store := storage_mocks.NewMockStore(ctrl)
			retriever := api_mocks.NewMockRetriever(ctrl)

			s, err := live.New(retriever, store)
			require.NoError(t, err)

			retriever.EXPECT().GetTodaysMatches().Return(tt.args.matchFacts, nil)

			err = s.CheckLiveMatches(tt.args.ctx)
			assert.Error(t, err)
		})
	}
}

func TestService_UpdateLiveMatches_SetLiveMatch_Error(t *testing.T) {
	const testErr = testError("error")

	type args struct {
		ctx        context.Context
		matchFacts []domain.MatchFacts
	}
	tests := []struct {
		name string
		args args
	}{
		{
			name: "error returned when setting live match",
			args: args{
				ctx: context.Background(),
				matchFacts: []domain.MatchFacts{
					{
						FormattedDate: time.Now().UTC().Format("02.01.2006"),
						Time:          time.Now().UTC().Add(time.Minute).Format("15:04"),
					},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctrl := gomock.NewController(t)
			defer ctrl.Finish()

			store := storage_mocks.NewMockStore(ctrl)
			retriever := api_mocks.NewMockRetriever(ctrl)

			s, err := live.New(retriever, store)
			require.NoError(t, err)

			retriever.EXPECT().GetTodaysMatches().Return(tt.args.matchFacts, nil)
			store.EXPECT().SetLiveMatch(tt.args.matchFacts[0].ToLiveMatch()).Return(testErr)

			err = s.CheckLiveMatches(tt.args.ctx)
			assert.Error(t, err)
		})
	}
}

func TestService_UpdateLiveMatches_Success(t *testing.T) {
	type args struct {
		ctx        context.Context
		matchFacts []domain.MatchFacts
	}
	tests := []struct {
		name string
		args args
	}{
		{
			name: "set live match successfully",
			args: args{
				ctx: context.Background(),
				matchFacts: []domain.MatchFacts{
					{
						FormattedDate: time.Now().UTC().Format("02.01.2006"),
						Time:          time.Now().UTC().Add(time.Minute).Format("15:04"),
					},
				},
			},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ctrl := gomock.NewController(t)
			defer ctrl.Finish()

			store := storage_mocks.NewMockStore(ctrl)
			retriever := api_mocks.NewMockRetriever(ctrl)

			s, err := live.New(retriever, store)
			require.NoError(t, err)

			retriever.EXPECT().GetTodaysMatches().Return(tt.args.matchFacts, nil)
			store.EXPECT().SetLiveMatch(tt.args.matchFacts[0].ToLiveMatch()).Return(nil)

			err = s.CheckLiveMatches(tt.args.ctx)
			assert.NoError(t, err)
		})
	}
}
