package aws

import (
	"context"
	"fmt"

	"github.com/cshep4/lambda-go/log"

	"github.com/cshep4/premier-predictor-functions/common/domain"
)

type (
	LiveMatchService interface {
		CheckLiveMatches(ctx context.Context) error
		RetrieveLiveMatches(context.Context) ([]domain.LiveMatch, error)
	}

	Handler struct {
		Service LiveMatchService
	}
)

func (h *Handler) CheckLiveMatches(ctx context.Context) error {
	err := h.Service.CheckLiveMatches(ctx)
	if err != nil {
		log.Error(ctx, "err_updating_live_matches", log.ErrorParam(err))

		return fmt.Errorf("update_live_matches: %w", err)
	}

	return nil
}

func (h *Handler) GetLiveMatches(ctx context.Context) ([]domain.LiveMatch, error) {
	liveMatches, err := h.Service.RetrieveLiveMatches(ctx)
	if err != nil {
		log.Error(ctx, "err_getting_live_matches", log.ErrorParam(err))

		return nil, fmt.Errorf("retrieve_live_matches: %w", err)
	}

	return liveMatches, nil
}
