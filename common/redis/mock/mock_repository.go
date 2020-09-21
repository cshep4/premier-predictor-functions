package mock

import (
	. "github.com/cshep4/premier-predictor-functions/common/domain"
	"github.com/stretchr/testify/mock"
	. "time"
)

type RedisRepository struct {
	mock.Mock
}

func (_m *RedisRepository) GetAllLiveMatches() ([]LiveMatch, error) {
	ret := _m.Called()

	var r []LiveMatch
	if rf, ok := ret.Get(0).(func() []LiveMatch); ok {
		r = rf()
	} else {
		r = ret.Get(0).([]LiveMatch)
	}

	return r, nil
}

func (_m *RedisRepository) GetLiveMatch(id string) LiveMatch {
	ret := _m.Called(id)

	var r LiveMatch
	if rf, ok := ret.Get(0).(func(string) LiveMatch); ok {
		r = rf(id)
	} else {
		r = ret.Get(0).(LiveMatch)
	}

	return r
}

func (_m *RedisRepository) SetLiveMatch(liveMatch LiveMatch) error {
	ret := _m.Called(liveMatch)

	var r error
	if rf, ok := ret.Get(0).(func(LiveMatch) error); ok {
		r = rf(liveMatch)
	} else {
		if ret.Get(0) == nil {
			r = nil
		} else {
			r = ret.Get(0).(error)
		}
	}

	return r
}

func (_m *RedisRepository) GetScoresUpdated() Time {
	ret := _m.Called()

	var r Time
	if rf, ok := ret.Get(0).(func() Time); ok {
		r = rf()
	} else {
		r = ret.Get(0).(Time)
	}

	return r
}

func (_m *RedisRepository) SetScoresUpdated() error {
	ret := _m.Called()

	var r error
	if rf, ok := ret.Get(0).(func() error); ok {
		r = rf()
	} else {
		if ret.Get(0) == nil {
			r = nil
		} else {
			r = ret.Get(0).(error)
		}
	}

	return r
}

func (_m *RedisRepository) Flush() error {
	ret := _m.Called()

	if ret.Get(0) == nil {
		return nil
	} else {
		return ret.Get(0).(error)
	}
}

func (_m *RedisRepository) SetIdempotency(key string) error {
	ret := _m.Called(key)

	var e error
	if rf, ok := ret.Get(0).(func(string) error); ok {
		e = rf(key)
	} else {
		e = ret.Get(0).(error)
	}

	return e
}

func (_m *RedisRepository) CheckIdempotency(key string) (bool, error) {
	ret := _m.Called(key)

	var r bool
	if rf, ok := ret.Get(0).(func(string) bool); ok {
		r = rf(key)
	} else {
		r = ret.Get(0).(bool)
	}
	var e error
	if rf, ok := ret.Get(1).(func(string) error); ok {
		e = rf(key)
	} else {
		e = ret.Get(1).(error)
	}

	return r, e
}

func (_m *RedisRepository) Close() {
	_m.Called()
}
