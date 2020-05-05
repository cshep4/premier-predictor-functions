package redis

import (
	"github.com/mediocregopher/radix/v3"
	"os"
	"github.com/cshep4/premier-predictor-functions/common/util"
	"time"
)

func connect(network, addr string) (radix.Conn, error) {
	password := os.Getenv("REDIS_PASSWORD")
	return radix.Dial(network, addr,
		radix.DialTimeout(30 * time.Second),
		radix.DialAuthPass(password),
	)
}

func Dial() radix.Conn {
	address := os.Getenv("REDIS_HOST") + ":" + os.Getenv("REDIS_PORT")
	conn, err := connect("tcp", address)
	util.CheckErr(err)

	return conn
}