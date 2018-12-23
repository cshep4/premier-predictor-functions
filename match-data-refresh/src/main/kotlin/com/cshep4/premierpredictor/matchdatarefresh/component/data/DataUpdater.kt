package com.cshep4.premierpredictor.matchdatarefresh.component.data

import com.cshep4.premierpredictor.matchdatarefresh.component.api.ApiRequester
import com.cshep4.premierpredictor.matchdatarefresh.component.matchfacts.MatchFactsUpdater
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class DataUpdater {
    @Autowired
    private lateinit var apiRequester: ApiRequester

    @Autowired
    private lateinit var matchFactsUpdater: MatchFactsUpdater

    @Autowired
    private lateinit var matchUpdater: MatchUpdater

    fun matchData(): List<MatchFacts> = runBlocking {
        val apiResult = apiRequester.retrieveFixtures()

        val matchUpdateResult = Channel<List<Match>>()
        val matchFactsUpdateResult = Channel<List<MatchFacts>>()

        launch {
            matchUpdateResult.send(matchUpdater.update(apiResult))
        }

        launch {
            matchFactsUpdateResult.send(matchFactsUpdater.update(apiResult))
        }

        System.out.println(matchUpdateResult.receive())
        System.out.println(matchFactsUpdateResult.receive())

        apiResult
    }
}
