package com.cshep4.premierpredictor.userscoreupdater.component.match

import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionReader
import com.cshep4.premierpredictor.userscoreupdater.data.Match
import com.cshep4.premierpredictor.userscoreupdater.data.PredictedMatch
import com.cshep4.premierpredictor.userscoreupdater.extensions.whenNullOrEmpty
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.FixtureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchReader {
    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    @Autowired
    private lateinit var predictionReader: PredictionReader

    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    fun retrieveAllFixtures(): List<Match> = fixtureRepository.findAll()

    fun retrieveAllMatchesWithPredictions(id: String) : List<PredictedMatch> {
        val matches = retrieveAllFixtures()

        matches.whenNullOrEmpty { return emptyList() }

        val predictions = predictionReader.retrievePredictionsByUserId(id)

        return predictionMerger.merge(matches, predictions)
    }
}