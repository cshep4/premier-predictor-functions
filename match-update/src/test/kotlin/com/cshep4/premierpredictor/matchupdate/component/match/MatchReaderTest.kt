package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.data.Prediction
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.FixturesRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
internal class MatchReaderTest {
    @Mock
    private lateinit var predictionReader: PredictionReader

    @Mock
    private lateinit var predictionMerger: PredictionMerger

    @Mock
    private lateinit var fixturesRepository: FixturesRepository

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @InjectMocks
    private lateinit var matchReader: MatchReader

    @Test
    fun `'retrieveAllMatches' should retrieve all matches`() {
        val matchEntity = MatchEntity()
        val matches = listOf(matchEntity)
        whenever(fixturesRepository.findAll()).thenReturn(matches)

        val result = matchReader.retrieveAllMatches()

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0], `is`(matchEntity.toDto()))
    }

    @Test
    fun `'retrieveTodaysMatches' should retrieve all matches that are being played today`() {
        val today = MatchEntity(dateTime = LocalDateTime.now())
        val tomorrow = MatchEntity(dateTime = LocalDateTime.now().plusDays(1))
        val yesterday = MatchEntity(dateTime = LocalDateTime.now().minusDays(1))
        val matches = listOf(today, tomorrow, yesterday)

        whenever(fixturesRepository.findAll()).thenReturn(matches)

        val result = matchReader.retrieveTodaysMatches()

        val expectedResult = listOf(today.toDto())

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'retrieveAllMatches' should return empty list if no matches exist`() {
        whenever(fixturesRepository.findAll()).thenReturn(emptyList())

        val result = matchReader.retrieveAllMatches()

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun `'retrieveAllMatchesWithPredictions' should retrieve all matches with predicted scorelines by user id`() {
        val matchEntities = listOf(MatchEntity(id = 1),
                MatchEntity(id = 2))

        val matches = matchEntities.map { it.toDto() }
        val predictedMatches = matches.map { it.toPredictedMatch() }

        val predictions = listOf(Prediction(matchId = 1, hGoals = 2, aGoals = 3),
                Prediction(matchId = 2, hGoals = 1, aGoals = 0))

        whenever(fixturesRepository.findAll()).thenReturn(matchEntities)
        whenever(predictionReader.retrievePredictionsByUserId(1)).thenReturn(predictions)
        whenever(predictionMerger.merge(matches, predictions)).thenReturn(predictedMatches)

        val result = matchReader.retrieveAllMatchesWithPredictions(1)

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0].id, `is`(1L))
        assertThat(result[1].id, `is`(2L))
    }

    @Test
    fun `'getAllMatchIds' gets a list of all match ids`() {
        val matches = listOf(MatchFactsEntity(id = "1"), MatchFactsEntity(id = "22"))
        val ids = matches.map { it.id }

        whenever(matchFactsRepository.findAll()).thenReturn(matches)

        val result = matchReader.getAllMatchIds()

        assertThat(result, `is`(ids))
    }

}