package com.cshep4.premierpredictor.matchupdate.component.livematch

import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.entity.LiveMatchEntity
import com.cshep4.premierpredictor.matchupdate.repository.redis.LiveMatchRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class LiveMatchDataHandlerTest {
    companion object {
        const val TEAM_1 = "Team 1"
        const val TEAM_2 = "Team 2"
        const val FORMATTED_DATE = "01.01.2000"
    }

    @Mock
    private lateinit var liveMatchRepository: LiveMatchRepository

    @InjectMocks
    private lateinit var liveMatchDataHandler: LiveMatchDataHandler

    @Test
    fun `'retrieveMatchIds' retrieves live matches from redis and returns set of the ids`() {
        val redisData = listOf(
                LiveMatchEntity(id = "1", hTeam = TEAM_1, aTeam = TEAM_2, formattedDate = FORMATTED_DATE),
                LiveMatchEntity(id = "2", hTeam = TEAM_1, aTeam = TEAM_2, formattedDate = FORMATTED_DATE),
                LiveMatchEntity(id = "3", hTeam = TEAM_1, aTeam = TEAM_2, formattedDate = FORMATTED_DATE),
                LiveMatchEntity(id = "4", hTeam = TEAM_1, aTeam = TEAM_2, formattedDate = FORMATTED_DATE)
        )

        whenever(liveMatchRepository.findAll()).thenReturn(redisData)

        val result = liveMatchDataHandler.retrieveMatchIds()

        val expectedResult = setOf("1", "2", "3", "4")

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'remove' iterates over each id and deletes the record from redis`() {
        liveMatchDataHandler.remove(listOf("123", "321", "111", "267"))

        verify(liveMatchRepository, times(4)).deleteById(any())
        verify(liveMatchRepository).deleteById("123")
        verify(liveMatchRepository).deleteById("321")
        verify(liveMatchRepository).deleteById("111")
        verify(liveMatchRepository).deleteById("267")
    }
}