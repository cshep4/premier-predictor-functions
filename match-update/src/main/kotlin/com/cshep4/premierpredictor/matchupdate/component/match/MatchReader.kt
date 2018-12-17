package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.PredictedMatch
import com.cshep4.premierpredictor.matchupdate.extensions.isToday
import com.cshep4.premierpredictor.matchupdate.extensions.whenNullOrEmpty
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.FixturesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchReader {
    @Autowired
    private lateinit var fixturesRepository: FixturesRepository

    @Autowired
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Autowired
    private lateinit var predictionReader: PredictionReader

    @Autowired
    private lateinit var predictionMerger: PredictionMerger

    fun retrieveAllMatches(): List<Match> = fixturesRepository.findAll()
            .map { it.toDto() }

    fun retrieveTodaysMatches(): List<Match> = retrieveAllMatches()
            .filter { it.dateTime!!.isToday() }

    fun retrieveAllMatchesWithPredictions(id: Long) : List<PredictedMatch> {
        val matches = retrieveAllMatches()

        matches.whenNullOrEmpty { return emptyList() }

        val predictions = predictionReader.retrievePredictionsByUserId(id)

        return predictionMerger.merge(matches, predictions)
    }

    fun getAllMatchIds(): List<String?> = matchFactsRepository.findAll()
            .map { it.id }
}