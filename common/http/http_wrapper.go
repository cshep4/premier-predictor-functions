package http

import (
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	. "net/http"
	"github.com/cshep4/premier-predictor-functions/common/util"
	"time"
)

type HttpWrapper struct{}

func InjectHttpWrapper() HttpWrapper {
	return HttpWrapper{}
}

func (h HttpWrapper) Get(url string) (*Response, error) {
	return http.Get(url)
}

func (h HttpWrapper) GetWithTimeout(url string, timeout time.Duration) (*Response, error) {
	client := http.Client{
		Timeout: timeout,
	}
	return client.Get(url)
}

func (h HttpWrapper) Put(url string, data interface{}) (*Response, error) {
	client := &http.Client{}

	jsonBody, _ := json.Marshal(data)
	body := bytes.NewBuffer(jsonBody)

	req, err := http.NewRequest(http.MethodPut, url, body)
	util.CheckErr(err)

	return client.Do(req)
}

func (h HttpWrapper) Post(url string, data interface{}) (*Response, error) {
	client := &http.Client{}

	jsonBody, _ := json.Marshal(data)
	body := bytes.NewBuffer(jsonBody)

	log.Println(string(jsonBody))

	req, err := http.NewRequest(http.MethodPost, url, body)
	util.CheckErr(err)

	req.Header.Add("Content-Type", "application/json")

	return client.Do(req)
}
