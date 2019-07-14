package main

import (
	"encoding/json"
	"fmt"
	"github.com/gocolly/colly"
	"github.com/stretchr/testify/assert"
	"log"
	. "premier-predictor-functions/common/domain"
	"premier-predictor-functions/data-retriever/factory"
	. "premier-predictor-functions/data-retriever/factory/mock"
	"testing"
)

func TestResponseIsOkWhenLiveMatchesAreRetrievedSuccessfully(t *testing.T) {
	liveMatches := []LiveMatch{{}, {}}
	liveMatchServiceFactory = MockLiveMatchServiceFactory(liveMatches)

	resp, err := Handler()

	expectedResponse, _ := json.Marshal(liveMatches)
	expectedBody := string(expectedResponse)

	assert.Equal(t, resp.StatusCode, 200)
	assert.Equal(t, resp.Body, expectedBody)
	assert.Equal(t, err, nil)
}

func TestResponseIsNotFoundWhenThereAreNoLiveMatchesPlaying(t *testing.T) {
	liveMatchServiceFactory = MockLiveMatchServiceFactory([]LiveMatch{})

	resp, err := Handler()

	assert.Equal(t, resp.StatusCode, 404)
	assert.Equal(t, resp.Body, "")
	assert.Equal(t, err, nil)
}

func TestIntegration(t *testing.T) {
	liveMatchServiceFactory = factory.LiveMatchServiceFactory{}

	resp, err := Handler()

	log.Println(resp)

	assert.Equal(t, err, nil)
}

func Test(t *testing.T) {
	c := colly.NewCollector()

	c.OnRequest(func(r *colly.Request) {
		fmt.Println("Visiting", r.URL)
	})

	c.OnError(func(_ *colly.Response, err error) {
		log.Println("Something went wrong:", err)
	})

	c.OnResponse(func(r *colly.Response) {
		fmt.Println("Visited", r.Request.URL)
	})

	//c.OnHTML("a[href]", func(e *colly.HTMLElement) {
	//	e.Request.Visit(e.Attr("href"))
	//})

	c.OnHTML("tr td:nth-of-type(1)", func(e *colly.HTMLElement) {
		fmt.Println("First column of a table row:", e.Text)
	})

	c.OnXML("//h1", func(e *colly.XMLElement) {
		fmt.Println(e.Text)
	})

	c.OnScraped(func(r *colly.Response) {
		fmt.Println("Finished", r.Request.URL)
	})

	// Start scraping on https://hackerspaces.org
	err := c.Visit("https://www.flashscore.com/football/england/premier-league/")
	fmt.Println(err)
}
