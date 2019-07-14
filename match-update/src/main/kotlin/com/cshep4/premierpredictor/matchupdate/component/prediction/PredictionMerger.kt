package com.cshep4.premierpredictor.matchupdate.component.prediction

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.PredictedMatch
import com.cshep4.premierpredictor.matchupdate.data.Prediction
import org.springframework.stereotype.Component

@Component
class PredictionMerger {
    fun merge(matches: List<Match>, predictions: List<Prediction>): List<PredictedMatch> {
        val predictedMatches = matches.map { it.toPredictedMatch() }

        predictedMatches.forEach {
            val id = it.id
            val prediction = predictions.firstOrNull{ p -> p.matchId == id } ?: Prediction(matchId = id, hGoals = null, aGoals = null)

            it.updatePrediction(prediction.hGoals, prediction.aGoals)
        }

        return predictedMatches
    }

}