package main

import (
	"testing"
)

func TestSend(t *testing.T) {
	//const text = "test"
	//
	//message := domain.Message{
	//	Text: text,
	//}
	//
	//jsonBody, _ := json.Marshal(message)
	//
	//req := APIGatewayProxyRequest{
	//	Body: string(jsonBody),
	//}
	//
	//t.Run("Response is OK when message is sent successfully", func(t *testing.T) {
	//	messageServiceFactory = MockMessageServiceFactory(text, nil)
	//
	//	resp, err := Handler(req)
	//
	//	assert.Equal(t, 200, resp.StatusCode)
	//	assert.Equal(t, "", resp.Body)
	//	assert.NoError(t, err)
	//})
	//
	//t.Run("Response is Bad Request when body is invalid", func(t *testing.T) {
	//	req := APIGatewayProxyRequest{
	//		Body: "test",
	//	}
	//
	//	resp, err := Handler(req)
	//
	//	assert.Equal(t, 400, resp.StatusCode)
	//	assert.Equal(t, "", resp.Body)
	//	assert.NoError(t, err)
	//})
	//
	//t.Run("Response is Internal Server Error when message is not sent successfully", func(t *testing.T) {
	//	messageServiceFactory = MockMessageServiceFactory(text, errors.New(""))
	//
	//	resp, err := Handler(req)
	//
	//	assert.Equal(t, 500, resp.StatusCode)
	//	assert.Equal(t, "", resp.Body)
	//	assert.NoError(t, err)
	//})
	//
	//t.Run("Integration test", func(t *testing.T) {
	//	messageServiceFactory = factory.MessageServiceFactory{}
	//
	//	resp, err := Handler(req)
	//
	//	log.Println(resp)
	//
	//	assert.NoError(t, err)
	//})
}
