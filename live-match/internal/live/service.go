package live

import (
	"context"
	"errors"
	"fmt"

	"github.com/cshep4/premier-predictor-functions/common/api"
	"github.com/cshep4/premier-predictor-functions/common/domain"
)

type (
	MatchUpdater interface {
		IsRunning(ctx context.Context) (bool, error)
		Start(ctx context.Context) error
	}
	Retriever interface {
		GetTodaysMatches() ([]domain.MatchFacts, error)
	}
	Store interface {
		GetAllLiveMatches() ([]domain.LiveMatch, error)
		SetLiveMatch(liveMatch domain.LiveMatch) error
		Flush() error
	}

	service struct {
		retriever    Retriever
		matchUpdater MatchUpdater
		store        Store
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(retriever Retriever, store Store, matchUpdater MatchUpdater) (*service, error) {
	switch {
	case retriever == nil:
		return nil, InvalidParameterError{Parameter: "retriever"}
	case store == nil:
		return nil, InvalidParameterError{Parameter: "store"}
	case matchUpdater == nil:
		return nil, InvalidParameterError{Parameter: "matchUpdater"}
	}

	return &service{
		retriever:    retriever,
		store:        store,
		matchUpdater: matchUpdater,
	}, nil
}

func (s *service) CheckLiveMatches(ctx context.Context) error {
	matchFacts, err := s.retriever.GetTodaysMatches()
	if err != nil {
		if errors.Is(err, api.ErrNotFound) {
			return nil
		}
		return fmt.Errorf("get_todays_matches: %w", err)
	}

	matchesPlaying := false
	for _, m := range matchFacts {
		shouldUpdate, err := m.ShouldBeUpdated()
		if err != nil {
			return fmt.Errorf("should_be_updated: %w", err)
		}
		if !shouldUpdate {
			continue
		}

		matchesPlaying = true
		if err := s.store.SetLiveMatch(m.ToLiveMatch()); err != nil {
			return fmt.Errorf("set_live_match: %w", err)
		}
	}

	if !matchesPlaying {
		return nil
	}

	running, err := s.matchUpdater.IsRunning(ctx)
	if err != nil {
		return fmt.Errorf("match_updater_is_running: %w", err)
	}
	if running {
		return nil
	}

	err = s.matchUpdater.Start(ctx)
	if err != nil {
		return fmt.Errorf("match_updater_start: %w", err)
	}

	return nil
}

func (s *service) RetrieveLiveMatches(context.Context) ([]domain.LiveMatch, error) {
	liveMatches, err := s.store.GetAllLiveMatches()
	if err != nil {
		return nil, fmt.Errorf("get_all_live_matches: %w", err)
	}

	return liveMatches, nil
}
