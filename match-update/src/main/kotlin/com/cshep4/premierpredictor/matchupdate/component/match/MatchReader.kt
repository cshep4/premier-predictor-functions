package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.PredictedMatch
import com.cshep4.premierpredictor.matchupdate.extensions.isToday
import com.cshep4.premierpredictor.matchupdate.extensions.whenNullOrEmpty
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchReader {
    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    @Autowired
    private lateinit var liveMatchServiceRepository: LiveMatchServiceRepository

    @Autowired
    private lateinit var predictionReader: PredictionReader

    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    fun retrieveAllFixtures(): List<Match> = fixtureRepository.findAll()

    fun retrieveTodaysMatches(): List<Match> = retrieveAllFixtures()
            .filter { it.dateTime!!.isToday() }

    fun retrieveAllMatchesWithPredictions(id: String) : List<PredictedMatch> {
        val matches = retrieveAllFixtures()

        matches.whenNullOrEmpty { return emptyList() }

        val predictions = predictionReader.retrievePredictionsByUserId(id)

        return predictionMerger.merge(matches, predictions)
    }

    fun getAllMatchIds(): List<String?> = liveMatchServiceRepository.findAll()
            .map { it.id }
}