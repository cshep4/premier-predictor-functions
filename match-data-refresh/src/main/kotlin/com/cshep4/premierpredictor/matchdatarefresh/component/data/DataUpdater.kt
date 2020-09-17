package com.cshep4.premierpredictor.matchdatarefresh.component.data

import com.cshep4.premierpredictor.matchdatarefresh.component.api.DataRetriever
import com.cshep4.premierpredictor.matchdatarefresh.component.matchfacts.MatchFactsUpdater
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.cshep4.premierpredictor.matchdatarefresh.extensions.isToday
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.time.format.DateTimeParseException

@Component
class DataUpdater {

    @Autowired
    @Qualifier("liveScore")
    private lateinit var dataRetriever: DataRetriever

    @Autowired
    private lateinit var matchFactsUpdater: MatchFactsUpdater

    @Autowired
    private lateinit var matchUpdater: MatchUpdater

    fun matchData(): List<MatchFacts> = runBlocking {
        val apiResult = dataRetriever.retrieveFixtures().map { validateDateTime(it) }

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

    private fun validateDateTime(match: MatchFacts): MatchFacts {
        if (match.time == "TBA") {
            match.time = "15:00"
        }

        return try {
            if (match.getDateTime()!!.isToday() && match.status == "Postp.") {
                match.formattedDate = "01.06.2020"
                match.time = "12:00"
                match.status = ""
            } else if (match.status == "Postp.") {
                match.status = ""
            }

            match
        } catch (e: DateTimeParseException) {
            match.formattedDate = "01.06.2020"
            match.time = "12:00"

            match
        }
    }
}
