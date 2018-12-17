package com.cshep4.premierpredictor.matchupdate.component.prediction

import com.cshep4.premierpredictor.matchupdate.data.Prediction
import com.cshep4.premierpredictor.matchupdate.repository.sql.PredictionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PredictionReader {
    @Autowired
    private lateinit var predictionsRepository: PredictionsRepository

    fun retrievePredictionsByUserId(id: Long) : List<Prediction> = predictionsRepository.findByUserId(id).map { it.toDto() }
}