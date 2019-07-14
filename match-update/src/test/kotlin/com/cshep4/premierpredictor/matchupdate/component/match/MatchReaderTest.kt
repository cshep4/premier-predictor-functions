package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionMerger
import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.Prediction
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchRepository
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
    private lateinit var fixtureRepository: FixtureRepository

    @Mock
    private lateinit var liveMatchRepository: LiveMatchRepository

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
    fun `'retrieveTodaysMatches' should retrieve all matches that are being played today`() {
        val today = Match(dateTime = LocalDateTime.now())
        val tomorrow = Match(dateTime = LocalDateTime.now().plusDays(1))
        val yesterday = Match(dateTime = LocalDateTime.now().minusDays(1))
        val matches = listOf(today, tomorrow, yesterday)

        whenever(fixtureRepository.findAll()).thenReturn(matches)

        val result = matchReader.retrieveTodaysMatches()

        val expectedResult = listOf(today)

        assertThat(result, `is`(expectedResult))
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

    @Test
    fun `'getAllMatchIds' gets a list of all match ids`() {
        val matches = listOf(MatchFacts(id = "1"), MatchFacts(id = "22"))
        val ids = matches.map { it.id }

        whenever(liveMatchRepository.findAll()).thenReturn(matches)

        val result = matchReader.getAllMatchIds()

        assertThat(result, `is`(ids))
    }

}