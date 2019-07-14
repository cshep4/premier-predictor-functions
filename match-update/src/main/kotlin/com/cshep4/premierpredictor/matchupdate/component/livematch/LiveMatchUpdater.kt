package com.cshep4.premierpredictor.matchupdate.component.livematch

import com.cshep4.premierpredictor.matchupdate.component.update.CommentaryRetriever
import com.cshep4.premierpredictor.matchupdate.component.update.MatchFactsRetriever
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LiveMatchUpdater {
    @Autowired
    private lateinit var liveMatchServiceRepository: LiveMatchServiceRepository

    @Autowired
    private lateinit var matchFactsRetriever: MatchFactsRetriever

    @Autowired
    private lateinit var commentaryRetriever: CommentaryRetriever

    fun retrieveLatest(id: String): MatchFacts? {
        val storedMatch = liveMatchServiceRepository
                .findById(id)

        return updateMatchFacts(storedMatch, id)
    }

    private fun updateMatchFacts(storedMatch: MatchFacts?, id: String): MatchFacts? = runBlocking {
        val matchChannel = Channel<MatchFacts?>()
        val commentaryChannel = Channel<Commentary?>()

        launch {
            matchChannel.send(matchFactsRetriever.getLatest(id))
        }

        launch {
            commentaryChannel.send(commentaryRetriever.getLatest(id))
        }

        updateMatchFacts(storedMatch, matchChannel.receive(), commentaryChannel.receive())
    }

    private fun updateMatchFacts(storedMatch: MatchFacts?, updatedMatch: MatchFacts?, updatedCommentary: Commentary?): MatchFacts? {
        val matchFacts = updatedMatch ?: storedMatch ?: return null

        matchFacts.commentary = when (updatedCommentary) {
            null -> storedMatch?.commentary
            else -> updatedCommentary
        }

        liveMatchServiceRepository.save(matchFacts)

        return matchFacts
    }
}