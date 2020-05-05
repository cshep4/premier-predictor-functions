package com.cshep4.premierpredictor.userscoreupdater.component.prediction

import com.cshep4.premierpredictor.userscoreupdater.data.Match
import com.cshep4.premierpredictor.userscoreupdater.data.Prediction
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.PredictionRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class PredictionReaderTest {
    companion object {
        const val USER_ID_1 = "‍️🆔"
        const val USER_ID_2 = "🤙"
        const val MATCH_ID_1 = "‍️🏟"
        const val MATCH_ID_2 = "⚽️"
        const val TEAM_1 = "‍️🥇"
        const val TEAM_2 = "🏆"
    }

    @Mock
    private lateinit var predictionRepository: PredictionRepository

    @Mock
    private lateinit var fixtureRepository: FixtureRepository

    @InjectMocks
    private lateinit var predictionReader: PredictionReader

    @Test
    fun `'retrievePredictionsByUserId' should retrieve all predictions for that user`() {
        val prediction = Prediction()
        val predictions = listOf(prediction)
        whenever(predictionRepository.findByUserId("1")).thenReturn(predictions)

        val result = predictionReader.retrievePredictionsByUserId("1")

        assertThat(result.isEmpty(), `is`(false))
        assertThat(result[0], `is`(prediction))
    }

    @Test
    fun `'retrievePredictionsByUserId' should return empty list if no predictions exist for that user id`() {
        whenever(predictionRepository.findByUserId("1")).thenReturn(emptyList())

        val result = predictionReader.retrievePredictionsByUserId("1")

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun `'retrieveAllPredictionsWithMatchResult' should get all predictions and all fixtures and merge them`() {
        val predictions = listOf(
                Prediction(userId = USER_ID_1, matchId = MATCH_ID_1, hGoals = 1, aGoals = 2),
                Prediction(userId = USER_ID_1, matchId = MATCH_ID_2, hGoals = 3, aGoals = 0),
                Prediction(userId = USER_ID_2, matchId = MATCH_ID_1, hGoals = 0, aGoals = 0),
                Prediction(userId = USER_ID_2, matchId = MATCH_ID_2, hGoals = 2, aGoals = 1)
        )

        val fixtures = listOf(
                Match(id = MATCH_ID_1, hTeam = TEAM_1, aTeam = TEAM_2, hGoals = 2, aGoals = 4, played = 1),
                Match(id = MATCH_ID_2, hTeam = TEAM_2, aTeam = TEAM_1, played = 0)
        )

        whenever(predictionRepository.findAll()).thenReturn(predictions)
        whenever(fixtureRepository.findAll()).thenReturn(fixtures)

        val result = predictionReader.retrieveAllPredictionsWithMatchResult()

        assertThat(result[0].userId, `is`(USER_ID_1))
        assertThat(result[0].matchId, `is`(MATCH_ID_1))
        assertThat(result[0].hTeam, `is`(TEAM_1))
        assertThat(result[0].aTeam, `is`(TEAM_2))
        assertThat(result[0].hGoals, `is`(2))
        assertThat(result[0].aGoals, `is`(4))
        assertThat(result[0].hPredictedGoals, `is`(1))
        assertThat(result[0].aPredictedGoals, `is`(2))

        assertThat(result[1].userId, `is`(USER_ID_1))
        assertThat(result[1].matchId, `is`(MATCH_ID_2))
        assertThat(result[1].hTeam, `is`(TEAM_2))
        assertThat(result[1].aTeam, `is`(TEAM_1))
        assertThat(result[1].hGoals, `is`(nullValue()))
        assertThat(result[1].aGoals, `is`(nullValue()))
        assertThat(result[1].hPredictedGoals, `is`(3))
        assertThat(result[1].aPredictedGoals, `is`(0))

        assertThat(result[2].userId, `is`(USER_ID_2))
        assertThat(result[2].matchId, `is`(MATCH_ID_1))
        assertThat(result[2].hTeam, `is`(TEAM_1))
        assertThat(result[2].aTeam, `is`(TEAM_2))
        assertThat(result[2].hGoals, `is`(2))
        assertThat(result[2].aGoals, `is`(4))
        assertThat(result[2].hPredictedGoals, `is`(0))
        assertThat(result[2].aPredictedGoals, `is`(0))

        assertThat(result[3].userId, `is`(USER_ID_2))
        assertThat(result[3].matchId, `is`(MATCH_ID_2))
        assertThat(result[3].hTeam, `is`(TEAM_2))
        assertThat(result[3].aTeam, `is`(TEAM_1))
        assertThat(result[3].hGoals, `is`(nullValue()))
        assertThat(result[3].aGoals, `is`(nullValue()))
        assertThat(result[3].hPredictedGoals, `is`(2))
        assertThat(result[3].aPredictedGoals, `is`(1))
    }

    @Test
    fun `'retrieveAllPredictionsWithMatchResult' should return empty list if no predictions exist`() {
        whenever(predictionRepository.findAll()).thenReturn(emptyList())

        val result = predictionReader.retrieveAllPredictionsWithMatchResult()

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun `'retrieveAllPredictionsWithMatchResult' should return empty list if no fixtures exist`() {
        val predictions = listOf(
                Prediction(userId = USER_ID_1, matchId = MATCH_ID_1, hGoals = 1, aGoals = 2),
                Prediction(userId = USER_ID_1, matchId = MATCH_ID_2, hGoals = 3, aGoals = 0),
                Prediction(userId = USER_ID_2, matchId = MATCH_ID_1, hGoals = 0, aGoals = 0),
                Prediction(userId = USER_ID_2, matchId = MATCH_ID_2, hGoals = 2, aGoals = 1)
        )

        whenever(predictionRepository.findAll()).thenReturn(predictions)
        whenever(fixtureRepository.findAll()).thenReturn(emptyList())

        val result = predictionReader.retrieveAllPredictionsWithMatchResult()

        assertThat(result.isEmpty(), `is`(true))
    }
}