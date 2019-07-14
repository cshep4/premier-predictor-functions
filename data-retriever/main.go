package main

import (
	"context"
	"encoding/json"
	"fmt"
	. "github.com/aws/aws-lambda-go/events"
	"github.com/chromedp/chromedp"
	"github.com/gocolly/colly"
	"log"
	"net/http"
	. "premier-predictor-functions/common/factory"
	"premier-predictor-functions/common/response"
	"premier-predictor-functions/common/util"
	"premier-predictor-functions/data-retriever/service/interfaces"
	"strings"
)

var liveMatchServiceFactory ServiceFactory

func Handler() (APIGatewayProxyResponse, error) {
	liveMatchService := liveMatchServiceFactory.Create().(interfaces.LiveMatchService)

	if liveMatches := liveMatchService.RetrieveLiveMatches(); len(liveMatches) > 0 {
		body, err := json.Marshal(liveMatches)
		util.CheckErr(err)

		return response.Ok(string(body))
	}

	return response.NotFound()
}

func main() {
	//liveMatchServiceFactory = LiveMatchServiceFactory{}
	//lambda.Start(Handler)

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

	c.OnHTML(".tname-home", func(e *colly.HTMLElement) {
		fmt.Println("Home Team:", strings.TrimSpace(e.Text))
	})

	c.OnHTML(".tname-away", func(e *colly.HTMLElement) {
		fmt.Println("Away Team:", strings.TrimSpace(e.Text))
	})

	hTeamDone := false
	c.OnHTML(".scoreboard", func(e *colly.HTMLElement) {
		if !hTeamDone {
			fmt.Println("Home Goals:", strings.TrimSpace(e.Text))
			hTeamDone = true
		} else {
			fmt.Println("Away Goals:", strings.TrimSpace(e.Text))
			hTeamDone = false
		}
	})

	c.OnHTML(".info-status", func(e *colly.HTMLElement) {
		fmt.Println("Status:", strings.TrimSpace(e.Text))
	})

	c.OnHTML("atomclock", func(e *colly.HTMLElement) {
		fmt.Println("Time:", strings.TrimSpace(e.Text))
	})

	c.OnHTML(".headerStrip", func(e *colly.HTMLElement) {
		fmt.Println("DateTime:", strings.TrimSpace(e.ChildText(".mstat-date")))
	})

	c.OnXML("//h1", func(e *colly.XMLElement) {
		fmt.Println(e.Text)
	})

	c.OnScraped(func(r *colly.Response) {
		fmt.Println("Finished", r.Request.URL)
	})

	//err := c.Visit("https://www.flashscore.com/match/UFOgEYGu/#match-summary")
	//fmt.Println(err)
	//
	//c.Wait()
	//
	//scrapeMatchSummary(c, "UFOgEYGu")
	//
	//err = c.Visit("https://www.flashscore.com/match/ARXKdR4H/#match-summary")
	//fmt.Println(err)
	//
	//c.Wait()
	//
	//scrapeMatchSummary(c, "ARXKdR4H")
	//
	//err = c.Visit("https://www.flashscore.com/match/dQGVdZPu/#match-summary")
	//fmt.Println(err)
	//
	//c.Wait()
	//
	//scrapeMatchSummary(c, "dQGVdZPu")

	//c.OnResponse(func(i *colly.Response) {
	//	if len(i.Body) == 0 {
	//		return
	//	}
	//	gr, err := gzip.NewReader(bytes.NewReader(i.Body))
	//	if err != nil {
	//		fmt.Println(err)
	//		return
	//	}
	//	defer gr.Close()
	//	d, err := ioutil.ReadAll(gr)
	//	if err != nil {
	//		fmt.Println(err)
	//		return
	//	}
	//	fmt.Println(d)
	//
	//})
	//
	//header := http.Header{
	//	"X-Fsign": []string{"SW9D1eZo"},
	//	//"Accept-Encoding": []string{"gzip"},
	//}
	//
	//// d_su_ - Summary
	//// d_st_ - Statistics
	//// d_li_ - Lineup
	//// d_ph_ - Commentary
	//// EXAMPLE - d_ph_UFOgEYGu_en_1 - CHECK AUTH HEADER (X-Fsign)
	//err := c.Request("GET", "https://d.flashscore.com/x/feed/g_1_hzwnsZ1O_en_1", nil, nil, header)
	//fmt.Println(err)

	// create context
	ctx, cancel := chromedp.NewContext(context.Background())
	defer cancel()

	// run task list
	var res string
	err := chromedp.Run(ctx,
		chromedp.Navigate(`https://www.flashscore.com/match/hzwnsZ1O/#match-summary`),
		chromedp.Text(`.detailMS`, &res, chromedp.NodeReady, chromedp.ByID),
	)
	if err != nil {
		log.Fatal(err)
	}

	log.Println(strings.TrimSpace(res))
}

func scrapeMatchSummary(c *colly.Collector, id string) {
	str := []string{}
	c.OnHTML(".detailMS", func(e *colly.HTMLElement) {
		e.ForEach(".detailMS__incidentRow", func(i int, elem *colly.HTMLElement) {
			if elem.ChildText(".time-box") != "" || elem.ChildText(".time-box-wide") != "" {
				//HOME OR AWAY
				if strings.Contains(elem.Attr("class"), "away") {
					fmt.Println("AWAY")
				} else {
					fmt.Println("HOME")
				}

				if elem.ChildText(".subincident-name") != "" { //Yellow/red cards
					fmt.Println("Minute:", getTime(elem))
					fmt.Println("Icon:", getIcon(elem))
					fmt.Println("Cause:", elem.ChildText(".subincident-name"))
					fmt.Println("Player:", elem.ChildText(".participant-name"))
				} else if elem.ChildText(".note-name") != "" { //Goals with assist
					fmt.Println("Minute:", getTime(elem))
					fmt.Println("Icon:", getIcon(elem))
					fmt.Println("Assist:", elem.ChildText(".note-name"))
					fmt.Println("Player:", elem.ChildText(".participant-name"))
				} else if elem.ChildText(".substitution-in-name") != "" { //Substitutions
					fmt.Println("Minute:", getTime(elem))
					fmt.Println("Icon:", getIcon(elem))
					fmt.Println("Sub Out:", elem.ChildText(".substitution-out-name"))
					fmt.Println("Sub In:", elem.ChildText(".substitution-in-name"))
				} else { //Goals without assist
					fmt.Println("Minute:", getTime(elem))
					fmt.Println("Icon:", getIcon(elem))
					fmt.Println("Player:", elem.ChildText(".participant-name"))
				}

				str = append(str, getTime(elem))

				fmt.Println("----------------------------------------------------------------------")
			}
		})
	})

	//Match Summary
	header := http.Header{
		"X-Fsign": []string{"SW9D1eZo"},
	}

	// d_su_ - Summary
	// d_st_ - Statistics
	// d_li_ - Lineup
	// d_ph_ - Commentary
	// EXAMPLE - d_ph_UFOgEYGu_en_1 - CHECK AUTH HEADER (X-Fsign)
	err := c.Request("GET", "https://d.flashscore.com/x/feed/d_su_"+id+"_en_1", nil, nil, header)
	fmt.Println(err)

	c.Wait()

	fmt.Println(str)
}

func getTime(elem *colly.HTMLElement) string {
	if elem.ChildText(".time-box") != "" {
		return elem.ChildText(".time-box")
	}

	return elem.ChildText(".time-box-wide")
}

func getIcon(elem *colly.HTMLElement) string {
	class := elem.ChildAttr(".icon-box", "class")

	if strings.Contains(class, "soccer-ball") {
		return "goal"
	} else if strings.Contains(class, "penalty-missed") {
		return "penalty-miss"
	} else if strings.Contains(class, " y-card") {
		return "yellow"
	} else if strings.Contains(class, " r-card") {
		return "red"
	} else if strings.Contains(class, "substitution") {
		return "substitution"
	} else if strings.Contains(class, "yr-card") {
		return "red"
	}

	return ""
}
