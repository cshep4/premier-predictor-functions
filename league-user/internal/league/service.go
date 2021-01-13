package league

import (
	"context"
	"fmt"
)

type (
	User struct {
		ID              string `json:"id"`
		FirstName       string `json:"firstName"`
		Surname         string `json:"surname"`
		PredictedWinner string `json:"predictedWinner"`
	}

	Store interface {
		GetLastPlace(ctx context.Context) (uint64, error)
		Store(ctx context.Context, user User, rank uint64) error
		Update(ctx context.Context, user User) error
	}

	service struct {
		store Store
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(store Store) (*service, error) {
	switch {
	case store == nil:
		return nil, InvalidParameterError{Parameter: "store"}
	}

	return &service{
		store: store,
	}, nil
}

func (s *service) CreateLeagueUser(ctx context.Context, user User) error {
	rank, err := s.store.GetLastPlace(ctx)
	if err != nil {
		return fmt.Errorf("get_last_place: %w", err)
	}

	err = s.store.Store(ctx, user, rank)
	if err != nil {
		return fmt.Errorf("store_league_user: %w", err)
	}

	return nil
}

func (s *service) UpdateLeagueUser(ctx context.Context, user User) error {
	err := s.store.Update(ctx, user)
	if err != nil {
		return fmt.Errorf("update_league_user: %w", err)
	}

	return nil
}
