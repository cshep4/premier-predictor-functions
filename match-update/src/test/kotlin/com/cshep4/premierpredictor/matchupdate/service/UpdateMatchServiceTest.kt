package com.cshep4.premierpredictor.matchupdate.service

import com.cshep4.premierpredictor.matchupdate.component.email.EmailDecorator
import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchDataHandler
import com.cshep4.premierpredictor.matchupdate.component.livematch.LiveMatchUpdater
import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.component.match.MatchWriter
import com.cshep4.premierpredictor.matchupdate.component.score.ScoresUpdatedReader
import com.cshep4.premierpredictor.matchupdate.component.score.UserScoreUpdater
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner.Silent::class)
internal class UpdateMatchServiceTest {
    @Mock
    private lateinit var liveMatchDataHandler: LiveMatchDataHandler

    @Mock
    private lateinit var liveMatchUpdater: LiveMatchUpdater

    @Mock
    private lateinit var matchWriter: MatchWriter

    @Mock
    private lateinit var matchReader: MatchReader

    @Mock
    private lateinit var userScoreUpdater: UserScoreUpdater

    @Mock
    private lateinit var emailDecorator: EmailDecorator

    @Mock
    private lateinit var scoresUpdatedReader: ScoresUpdatedReader

    @InjectMocks
    private lateinit var updateMatchService: UpdateMatchService

    @Test
    fun `'updateLiveMatches' gets the live match ids from redis and returns without updating if there are none currently playing`() {
        whenever(liveMatchDataHandler.retrieveMatchIds()).thenReturn(emptySet())

        val result = updateMatchService.updateLiveMatches()

        assertThat(result, `is`(true))
        verify(liveMatchDataHandler).retrieveMatchIds()
    }

    @Test
    fun `'updateLiveMatches' gets the live match ids from redis and retrieves latest data for games that are playing`() {
        whenever(liveMatchDataHandler.retrieveMatchIds()).thenReturn(setOf("1", "44"))

        whenever(liveMatchUpdater.retrieveLatest("1")).thenReturn(MatchFacts())
        whenever(liveMatchUpdater.retrieveLatest("44")).thenReturn(MatchFacts())

        val result = updateMatchService.updateLiveMatches()

        assertThat(result, `is`(true))
        verify(liveMatchDataHandler).retrieveMatchIds()
        verify(liveMatchUpdater).retrieveLatest("1")
        verify(liveMatchUpdater).retrieveLatest("44")
    }

    @Test
    fun `'updateLiveMatches' returns false if matches are not retrieved`() {
        whenever(liveMatchDataHandler.retrieveMatchIds()).thenReturn(setOf("1", "44"))

        whenever(liveMatchUpdater.retrieveLatest(any())).thenReturn(null)

        val result = updateMatchService.updateLiveMatches()

        assertThat(result, `is`(false))
        verify(liveMatchDataHandler).retrieveMatchIds()
        verify(liveMatchUpdater).retrieveLatest("1")
        verify(liveMatchUpdater).retrieveLatest("44")
    }

    @Test
    fun `'updateLiveMatches' saves matches to dynamo db after retrieving latest data for games that are playing`() {
        whenever(liveMatchDataHandler.retrieveMatchIds()).thenReturn(setOf("1", "44"))

        val updatedMatches = listOf(MatchFacts(), MatchFacts())

        whenever(liveMatchUpdater.retrieveLatest("1")).thenReturn(updatedMatches[0])
        whenever(liveMatchUpdater.retrieveLatest("44")).thenReturn(updatedMatches[1])

        val result = updateMatchService.updateLiveMatches()

        assertThat(result, `is`(true))
        verify(matchWriter).matchFacts(updatedMatches)
        verify(matchWriter, times(0)).matches(any())
        verify(liveMatchDataHandler, times(0)).remove(any())
        verify(userScoreUpdater, times(0)).update()
    }

    @Test
    fun `'updateLiveMatches' saves matches to sql db if match has finished`() {
        val match = mockMatchUpdateWithFinishedMatch()

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))

        val result = updateMatchService.updateLiveMatches()

        val expectedSavedMatch = listOf(match.toMatch())

        assertThat(result, `is`(true))
        verify(matchWriter).matches(expectedSavedMatch)
    }

    @Test
    fun `'updateLiveMatches' removes liveMatch from redis db if match has finished`() {
        val match = mockMatchUpdateWithFinishedMatch()

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))

        val result = updateMatchService.updateLiveMatches()

        val expectedDeletedMatch = listOf(match.id!!)

        assertThat(result, `is`(true))
        verify(liveMatchDataHandler).remove(expectedDeletedMatch)
    }

    @Test
    fun `'updateLiveMatches' gets matches from dynamodb, checks if all today's matches have finished and updates scores`() {
        mockMatchUpdateWithFinishedMatch()

        val match = Match(hGoals = 1, aGoals = 2, dateTime = LocalDateTime.now())

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))
        whenever(matchReader.retrieveTodaysMatches()).thenReturn(listOf(match))

        runBlocking {
            updateMatchService.updateLiveMatches()
        }

        verify(emailDecorator).operationNotification(userScoreUpdater::update)
    }

    @Test
    fun `'updateLiveMatches' gets matches from dynamodb, checks if all today's matches have finished and does not update scores if its already been done today`() {
        mockMatchUpdateWithFinishedMatch()

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))

        runBlocking {
            updateMatchService.updateLiveMatches()
        }

        verify(matchReader, times(0)).retrieveTodaysMatches()
        verify(emailDecorator, times(0)).operationNotification(userScoreUpdater::update)
    }

    @Test
    fun `'updateLiveMatches' gets matches from dynamodb, checks if all today's matches have finished and does not update scores if there are still matches to finish`() {
        mockMatchUpdateWithFinishedMatch()

        val match = Match(dateTime = LocalDateTime.now())

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))
        whenever(matchReader.retrieveTodaysMatches()).thenReturn(listOf(match))

        runBlocking {
            updateMatchService.updateLiveMatches()
        }

        verify(emailDecorator, times(0)).operationNotification(userScoreUpdater::update)
    }

    @Test
    fun `'updateLiveMatches' gets matches from dynamodb, checks if all today's matches have finished and does not update scores if there are no matches`() {
        mockMatchUpdateWithFinishedMatch()

        whenever(scoresUpdatedReader.scoresLastUpdated()).thenReturn(LocalDate.now().minusDays(1))
        whenever(matchReader.retrieveTodaysMatches()).thenReturn(emptyList())

        runBlocking {
            updateMatchService.updateLiveMatches()
        }

        verify(emailDecorator, times(0)).operationNotification(userScoreUpdater::update)
    }

    private fun mockMatchUpdateWithFinishedMatch(): MatchFacts {
        whenever(liveMatchDataHandler.retrieveMatchIds()).thenReturn(setOf("1", "44"))

        val match1 = MatchFacts(id = "1", localTeamName = "test", visitorTeamName = "test", status = "FT", week = "1", time = "12:00", formattedDate = "01.01.2000")
        val match2 = MatchFacts()

        val updatedMatches = listOf(match1, match2)

        whenever(liveMatchUpdater.retrieveLatest("1")).thenReturn(updatedMatches[0])
        whenever(liveMatchUpdater.retrieveLatest("44")).thenReturn(updatedMatches[1])

        return match1
    }
}
