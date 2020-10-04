package com.cshep4.premierpredictor.matchupdate.service

import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchDataHandler
import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchUpdater
import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.component.match.MatchWriter
import com.cshep4.premierpredictor.matchupdate.component.notify.NotificationDecorator
import com.cshep4.premierpredictor.matchupdate.component.score.ScoresUpdatedReader
import com.cshep4.premierpredictor.matchupdate.component.score.UserScoreUpdater
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.domain.State
import com.cshep4.premierpredictor.matchupdate.extensions.anyOrEmpty
import com.cshep4.premierpredictor.matchupdate.extensions.isToday
import com.cshep4.premierpredictor.matchupdate.extensions.whenNotNullNorEmpty
import com.cshep4.premierpredictor.matchupdate.extensions.whenNullOrEmpty
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
    private lateinit var notificationDecorator: NotificationDecorator

    fun updateLiveMatches(): State {
        liveMatchDataHandler.retrieveMatchIds().whenNotNullNorEmpty { liveMatchIds ->
            val updatedMatches = liveMatchIds
                    .mapNotNull { liveMatchUpdater.retrieveLatest(it) }

            if (updatedMatches.isEmpty()) {
                return State.END
            }

            matchWriter.matchFacts(updatedMatches)

            val finishedMatches = updatedMatches.filter { it.status == "FT" }
                    .map { it.toMatch() }
                    .ifEmpty { return State.WAIT }

            matchWriter.fixtures(finishedMatches)
            liveMatchDataHandler.remove(finishedMatches.map { it.id })

            if (matchReader.retrieveTodaysMatches().anyOrEmpty { it.hGoals == null && it.aGoals == null }) {
                return State.WAIT
            }

            return State.UPDATE_USER_SCORES
        }

        return State.END
    }
}