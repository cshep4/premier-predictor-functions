package live

import (
	"context"
	"errors"
	"fmt"

	"github.com/cshep4/premier-predictor-functions/common/api"
	"github.com/cshep4/premier-predictor-functions/common/domain"
)

type (
	Retriever interface {
		GetTodaysMatches() ([]domain.MatchFacts, error)
	}
	Store interface {
		GetAllLiveMatches() ([]domain.LiveMatch, error)
		SetLiveMatch(liveMatch domain.LiveMatch) error
		Flush() error
	}

	service struct {
		retriever Retriever
		store     Store
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(retriever Retriever, store Store) (*service, error) {
	switch {
	case retriever == nil:
		return nil, InvalidParameterError{Parameter: "retriever"}
	case store == nil:
		return nil, InvalidParameterError{Parameter: "store"}
	}

	return &service{
		retriever: retriever,
		store:     store,
	}, nil
}

func (s *service) CheckLiveMatches(context.Context) error {
	matchFacts, err := s.retriever.GetTodaysMatches()
	if err != nil {
		if errors.Is(err, api.ErrNotFound) {
			return nil
		}
		return fmt.Errorf("get_todays_matches: %w", err)
	}

	for _, m := range matchFacts {
		shouldUpdate, err := m.ShouldBeUpdated()
		if err != nil {
			return fmt.Errorf("should_be_updated: %w", err)
		}
		if !shouldUpdate {
			continue
		}

		if err := s.store.SetLiveMatch(m.ToLiveMatch()); err != nil {
			return fmt.Errorf("set_live_match: %w", err)
		}
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
