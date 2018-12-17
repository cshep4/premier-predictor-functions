package interfaces

import (
	. "net/http"
)

type HttpWrapper interface {
	Get(url string) (*Response, error)
	Put(url string, data interface{}) (*Response, error)
}