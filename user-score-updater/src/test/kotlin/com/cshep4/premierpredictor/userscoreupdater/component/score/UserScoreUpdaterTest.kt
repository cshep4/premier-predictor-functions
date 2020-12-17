package com.cshep4.premierpredictor.userscoreupdater.component.score

import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionReader
import com.cshep4.premierpredictor.userscoreupdater.component.time.Time
import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import com.cshep4.premierpredictor.userscoreupdater.data.MatchPredictionResult
import com.cshep4.premierpredictor.userscoreupdater.data.User
import com.cshep4.premierpredictor.userscoreupdater.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.LeagueRepository
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.UserRepository
import com.cshep4.premierpredictor.userscoreupdater.repository.redis.ScoresUpdatedRepository
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
    private lateinit var leagueRepository: LeagueRepository

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var scoreUpdater: ScoreUpdater

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

        scoreUpdater.update()

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

        val result = scoreUpdater.update()

        assertThat(result, `is`(users))
    }

    @Test
    fun `'update' will build the overall league table and store`() {
        val storedUsers = listOf(User())
        val users = listOf(
                User(
                        id = "1",
                        firstName = "first name 1",
                        surname = "surname 1",
                        predictedWinner = "1 winner",
                        score = 6
                ),
                User(
                        id = "2",
                        firstName = "first name 2",
                        surname = "surname 2",
                        predictedWinner = "2 winner",
                        score = 8
                ),
                User(
                        id = "3",
                        firstName = "first name 3",
                        surname = "surname 3",
                        predictedWinner = "3 winner",
                        score = 6
                ),
                User(
                        id = "4",
                        firstName = "first name 4",
                        surname = "surname 4",
                        predictedWinner = "4 winner",
                        score = 2
                ),
                User(
                        id = "5",
                        firstName = "first name 5",
                        surname = "surname 5",
                        predictedWinner = "5 winner",
                        score = 6
                )
        )


        val expectedLeagueUsers = listOf(
                LeagueUser(
                        id = "2",
                        rank = 1,
                        name = "first name 2 surname 2",
                        predictedWinner = "2 winner",
                        score = 8
                ),
                LeagueUser(
                        id = "1",
                        rank = 2,
                        name = "first name 1 surname 1",
                        predictedWinner = "1 winner",
                        score = 6
                ),
                LeagueUser(
                        id = "3",
                        rank = 2,
                        name = "first name 3 surname 3",
                        predictedWinner = "3 winner",
                        score = 6
                ),
                LeagueUser(
                        id = "5",
                        rank = 2,
                        name = "first name 5 surname 5",
                        predictedWinner = "5 winner",
                        score = 6
                ),
                LeagueUser(
                        id = "4",
                        rank = 5,
                        name = "first name 4 surname 4",
                        predictedWinner = "4 winner",
                        score = 2
                )
        )

        val predictedMatches = listOf(MatchPredictionResult(hGoals = 1, aGoals = 1))

        whenever(userRepository.findAll()).thenReturn(storedUsers)
        whenever(predictionReader.retrieveAllPredictionsWithMatchResult()).thenReturn(predictedMatches)
        whenever(leagueTableScoreCalculator.calculate(storedUsers, predictedMatches)).thenReturn(users)
        whenever(matchScoreCalculator.calculate(users, predictedMatches)).thenReturn(users)
        whenever(winnerScoreCalculator.calculate(users)).thenReturn(users)
        whenever(time.localDateNow()).thenReturn(LocalDate.now())

        scoreUpdater.update()

        verify(leagueRepository).save(expectedLeagueUsers)
    }
}