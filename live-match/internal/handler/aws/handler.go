package aws

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"

	"github.com/aws/aws-lambda-go/events"
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

func (h *Handler) GetLiveMatches(ctx context.Context) (events.APIGatewayProxyResponse, error) {
	liveMatches, err := h.Service.RetrieveLiveMatches(ctx)
	if err != nil {
		log.Error(ctx, "err_getting_live_matches", log.ErrorParam(err))

		return events.APIGatewayProxyResponse{
			StatusCode: http.StatusInternalServerError,
			Headers:    map[string]string{"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
		}, fmt.Errorf("retrieve_live_matches: %w", err)
	}

	body, err := json.Marshal(liveMatches)
	if err != nil {
		log.Error(ctx, "err_json_marshal", log.ErrorParam(err))

		return events.APIGatewayProxyResponse{
			StatusCode: http.StatusInternalServerError,
			Headers:    map[string]string{"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
		}, fmt.Errorf("json_marshal: %w", err)
	}

	return events.APIGatewayProxyResponse{
		StatusCode: http.StatusOK,
		Headers:    map[string]string{"Content-Type": "application/json", "Access-Control-Allow-Origin": "*"},
		Body:       string(body),
	}, nil
}
