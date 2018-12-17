package http

import (
	"bytes"
	"encoding/json"
	"net/http"
	. "net/http"
	"premier-predictor-functions/common/util"
)

type HttpWrapper struct {}

func InjectHttpWrapper() HttpWrapper {
	return HttpWrapper{}
}

func (h HttpWrapper) Get(url string) (*Response, error) {
	return http.Get(url)
}

func (h HttpWrapper) Put(url string, data interface{}) (*Response, error) {
	client := &http.Client{}

	jsonBody, _ := json.Marshal(data)
	body := bytes.NewBuffer(jsonBody)

	req, err := http.NewRequest(http.MethodPut, url, body)
	util.CheckErr(err)

	return client.Do(req)
}