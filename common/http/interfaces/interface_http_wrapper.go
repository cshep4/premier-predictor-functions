package interfaces

import (
	. "net/http"
	"time"
)

type HttpWrapper interface {
	Get(url string) (*Response, error)
	GetWithTimeout(url string, timeout time.Duration) (*Response, error)
	Put(url string, data interface{}) (*Response, error)
	Post(url string, data interface{}) (*Response, error)
}
