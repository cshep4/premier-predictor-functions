package com.cshep4.premierpredictor.matchupdate.component.prediction

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.MatchPredictionResult
import com.cshep4.premierpredictor.matchupdate.data.Prediction
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.matchupdate.repository.mongo.PredictionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PredictionReader {
    @Autowired
    private lateinit var predictionRepository: PredictionRepository

    @Autowired
    private lateinit var fixtureRepository: FixtureRepository

    fun retrievePredictionsByUserId(id: String) : List<Prediction> = predictionRepository.findByUserId(id)

    fun retrieveAllPredictionsWithMatchResult(): List<MatchPredictionResult> {
        val predictions = predictionRepository.findAll()

        if (predictions.isEmpty()) {
            return emptyList()
        }

        val fixtures = fixtureRepository.findAll()

        if (fixtures.isEmpty()) {
            return emptyList()
        }

        return predictions.mapNotNull { buildMatchPredictionResult(it, fixtures) }
    }

    fun buildMatchPredictionResult(prediction: Prediction, fixtures: List<Match>): MatchPredictionResult? {
        val fixture = fixtures.firstOrNull { it.id == prediction.matchId } ?: return null

        return MatchPredictionResult(
                userId = prediction.userId!!,
                matchId = prediction.matchId!!,
                hTeam = fixture.hTeam,
                aTeam = fixture.aTeam,
                hGoals = fixture.hGoals,
                aGoals = fixture.aGoals,
                hPredictedGoals = prediction.hGoals,
                aPredictedGoals = prediction.aGoals,
                matchday = fixture.matchday
        )
    }
}