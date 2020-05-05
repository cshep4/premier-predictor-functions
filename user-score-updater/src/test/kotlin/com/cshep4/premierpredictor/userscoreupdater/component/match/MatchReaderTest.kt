package com.cshep4.premierpredictor.userscoreupdater.component.match

import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionReader
import com.cshep4.premierpredictor.userscoreupdater.data.Match
import com.cshep4.premierpredictor.userscoreupdater.data.Prediction
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.FixtureRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MatchReaderTest {
    @Mock
    private lateinit var predictionReader: PredictionReader

    @Mock
    private lateinit var predictionMerger: PredictionMerger

    @Mock
    private lateinit var fixtureRepository: FixtureRepository

    @InjectMocks
    private lateinit var matchReader: MatchReader

    @Test
    fun `'retrieveAllFixtures' should retrieve all matches`() {
        val fixture = Match()
        val matches = listOf(fixture)
        whenever(fixtureRepository.findAll()).thenReturn(matches)

        val result = matchReader.retrieveAllFixtures()

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0], `is`(fixture))
    }

    @Test
    fun `'retrieveAllMatches' should return empty list if no matches exist`() {
        whenever(fixtureRepository.findAll()).thenReturn(emptyList())

        val result = matchReader.retrieveAllFixtures()

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun `'retrieveAllMatchesWithPredictions' should retrieve all matches with predicted scorelines by user id`() {
        val fixtures = listOf(Match(id = "1"),
                Match(id = "2"))

        val predictedFixtures = fixtures.map { it.toPredictedMatch() }

        val predictions = listOf(Prediction(matchId = "1", hGoals = 2, aGoals = 3),
                Prediction(matchId = "2", hGoals = 1, aGoals = 0))

        whenever(fixtureRepository.findAll()).thenReturn(fixtures)
        whenever(predictionReader.retrievePredictionsByUserId("1")).thenReturn(predictions)
        whenever(predictionMerger.merge(fixtures, predictions)).thenReturn(predictedFixtures)

        val result = matchReader.retrieveAllMatchesWithPredictions("1")

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0].id, `is`("1"))
        assertThat(result[1].id, `is`("2"))
    }
}