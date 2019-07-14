package com.cshep4.premierpredictor.matchupdate.component.score

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.MatchPredictionResult
import com.cshep4.premierpredictor.matchupdate.data.User
import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.matchupdate.repository.mongo.UserRepository
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
internal class UserScoreUpdaterTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var predictionReader: PredictionReader

    @Mock
    private lateinit var leagueTableScoreCalculator: LeagueTableScoreCalculator

    @Mock
    private lateinit var matchScoreCalculator: MatchScoreCalculator

    @Mock
    private lateinit var winnerScoreCalculator: WinnerScoreCalculator

    @Mock
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var userScoreUpdater: UserScoreUpdater

    @Test
    fun `'update' will deduplicate predictions, get a list of users, add score for individual matches, then league position, then winner and save back to db`() {
        val users = listOf(User())

        val predictedMatches = listOf(MatchPredictionResult(hGoals = 1, aGoals = 1))

        val scoresUpdatedEntity = ScoresUpdatedEntity(id = 1, lastUpdated = LocalDate.now())

        whenever(userRepository.findAll()).thenReturn(users)
        whenever(predictionReader.retrieveAllPredictionsWithMatchResult()).thenReturn(predictedMatches)
        whenever(leagueTableScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(matchScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(winnerScoreCalculator.calculate(users)).thenReturn(users)
        whenever(time.localDateNow()).thenReturn(scoresUpdatedEntity.lastUpdated)

        userScoreUpdater.update()

        verify(leagueTableScoreCalculator).calculate(users, predictedMatches)
        verify(matchScoreCalculator).calculate(users, predictedMatches)
        verify(winnerScoreCalculator).calculate(users)
        verify(userRepository).save(users)
        verify(scoresUpdatedRepository).save(scoresUpdatedEntity)
    }

    @Test
    fun `'update' returns a list of saved users`() {
        val users = listOf(User())

        val predictedMatches = listOf(MatchPredictionResult(hGoals = 1, aGoals = 1))

        whenever(userRepository.findAll()).thenReturn(users)
        whenever(predictionReader.retrieveAllPredictionsWithMatchResult()).thenReturn(predictedMatches)
        whenever(leagueTableScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(matchScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(winnerScoreCalculator.calculate(users)).thenReturn(users)

        whenever(time.localDateNow()).thenReturn(LocalDate.now())

        val result = userScoreUpdater.update()

        assertThat(result, `is`(users))
    }
}