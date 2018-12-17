package com.cshep4.premierpredictor.matchupdate.service

import com.cshep4.premierpredictor.matchupdate.component.email.EmailDecorator
import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchDataHandler
import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchUpdater
import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.component.match.MatchWriter
import com.cshep4.premierpredictor.matchupdate.component.score.ScoresUpdatedReader
import com.cshep4.premierpredictor.matchupdate.component.score.UserScoreUpdater
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.extensions.isToday
import com.cshep4.premierpredictor.matchupdate.extensions.whenNotNullNorEmpty
import com.cshep4.premierpredictor.matchupdate.extensions.whenNullOrEmpty
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UpdateMatchService {
    @Autowired
    private lateinit var liveMatchDataHandler: LiveMatchDataHandler

    @Autowired
    private lateinit var liveMatchUpdater: LiveMatchUpdater

    @Autowired
    private lateinit var matchWriter: MatchWriter

    @Autowired
    private lateinit var matchReader: MatchReader

    @Autowired
    private lateinit var userScoreUpdater: UserScoreUpdater

    @Autowired
    private lateinit var scoresUpdatedReader: ScoresUpdatedReader

    @Autowired
    private lateinit var emailDecorator: EmailDecorator

    fun updateLiveMatches(): Boolean {
        liveMatchDataHandler.retrieveMatchIds().whenNotNullNorEmpty { liveMatchIds ->
            val updatedMatches = liveMatchIds
                    .mapNotNull { liveMatchUpdater.retrieveLatest(it) }

            updatedMatches.whenNullOrEmpty {
                return false
            }

            matchWriter.matchFacts(updatedMatches)

            updatedMatches.filter { it.status == "FT" }
                    .map { it.toMatch() }
                    .whenNotNullNorEmpty { processFinishedMatches(it) }
        }

        return true
    }

    private fun processFinishedMatches(matches: Collection<Match>) {
        matchWriter.matches(matches)
        liveMatchDataHandler.remove(matches.map { it.id.toString() })

        GlobalScope.launch {
            if (!scoresUpdatedReader.scoresLastUpdated().isToday()) {
                matchReader.retrieveTodaysMatches()
                        .whenNotNullNorEmpty { updateScoresIfMatchesHaveFinished(it) }
            }
        }

    }

    private fun updateScoresIfMatchesHaveFinished(todaysMatches: Collection<Match>) {
        todaysMatches.filter { it.hGoals == null && it.aGoals == null }
                .whenNullOrEmpty { emailDecorator.operationNotification(userScoreUpdater::update) }
    }
}