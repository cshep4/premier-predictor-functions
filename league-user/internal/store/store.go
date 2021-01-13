package store

import (
	"context"
	"fmt"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"go.mongodb.org/mongo-driver/x/bsonx"

	"github.com/cshep4/premier-predictor-functions/league-user/internal/league"
)

const (
	db         = "league"
	collection = "overall"
)

type (
	leagueUser struct {
		ID              primitive.ObjectID `bson:"_id"`
		Name            string             `bson:"name"`
		PredictedWinner string             `bson:"predictedWinner"`
		Rank            uint64             `bson:"rank"`
		Score           uint64             `bson:"score"`
	}

	store struct {
		client     *mongo.Client
		collection *mongo.Collection
	}

	// InvalidParameterError is returned when a required parameter passed to New is invalid.
	InvalidParameterError struct {
		Parameter string
	}
)

func (i InvalidParameterError) Error() string {
	return fmt.Sprintf("invalid parameter %s", i.Parameter)
}

func New(ctx context.Context, client *mongo.Client) (*store, error) {
	if client == nil {
		return nil, InvalidParameterError{Parameter: "client"}
	}

	s := &store{
		client:     client,
		collection: client.Database(db).Collection(collection),
	}

	if err := s.ping(ctx); err != nil {
		return nil, err
	}

	if err := s.ensureIndexes(ctx); err != nil {
		return nil, err
	}

	return s, nil
}

func (s *store) ensureIndexes(ctx context.Context) error {
	_, err := s.collection.Indexes().
		CreateOne(
			ctx,
			mongo.IndexModel{
				Keys: bsonx.Doc{
					{Key: "score", Value: bsonx.Int64(1)},
				},
				Options: options.Index().
					SetName("scoreIdx").
					SetUnique(false).
					SetBackground(true),
			},
		)
	if err != nil {
		return err
	}

	return nil
}

func (s *store) GetLastPlace(ctx context.Context) (uint64, error) {
	lu, err := s.getLastPlacedUser(ctx)
	if err != nil {
		return 0, fmt.Errorf("get_last_placed_user: %w", err)
	}

	if lu.Score == 0 {
		return lu.Rank, nil
	}

	count, err := s.collection.CountDocuments(ctx, bson.D{})
	if err != nil {
		return 0, fmt.Errorf("count_documents: %w", err)
	}

	return uint64(count), nil
}

func (s *store) getLastPlacedUser(ctx context.Context) (*leagueUser, error) {
	limit := int64(1)

	cur, err := s.collection.Find(ctx, bson.D{}, &options.FindOptions{
		Limit: &limit,
		Sort:  bson.D{{Key: "score", Value: 1}},
	})
	if err != nil {
		return nil, fmt.Errorf("find: %w", err)
	}

	var lu leagueUser
	defer cur.Close(ctx)

	for cur.Next(ctx) {
		if err := cur.Decode(&lu); err != nil {
			return nil, fmt.Errorf("decode: %w", err)
		}
	}

	if err := cur.Err(); err != nil {
		return nil, fmt.Errorf("cursor_err: %w", err)
	}

	return &lu, nil
}

func (s *store) Store(ctx context.Context, user league.User, rank uint64) error {
	id, err := primitive.ObjectIDFromHex(user.ID)
	if err != nil {
		return fmt.Errorf("object_id_from_hex: %w", err)
	}

	_, err = s.collection.InsertOne(ctx, leagueUser{
		ID:              id,
		Name:            fmt.Sprintf("%s %s", user.FirstName, user.Surname),
		PredictedWinner: user.PredictedWinner,
		Rank:            rank,
		Score:           0,
	})
	if err != nil {
		return fmt.Errorf("insert_one: %w", err)
	}

	return nil
}

func (s *store) ping(ctx context.Context) error {
	ctx, _ = context.WithTimeout(ctx, 2*time.Second)
	return s.client.Ping(ctx, nil)
}

func (s *store) Close(ctx context.Context) error {
	return s.client.Disconnect(ctx)
}
