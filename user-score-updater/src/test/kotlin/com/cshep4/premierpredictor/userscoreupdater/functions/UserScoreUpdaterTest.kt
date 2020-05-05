package com.cshep4.premierpredictor.userscoreupdater.functions

import com.cshep4.premierpredictor.userscoreupdater.domain.Request
import com.cshep4.premierpredictor.userscoreupdater.service.UserScoreUpdaterService
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.OK

@RunWith(MockitoJUnitRunner::class)
internal class UserScoreUpdaterTest {
    @Mock
    private lateinit var userScoreUpdaterService: UserScoreUpdaterService

    @InjectMocks
    private lateinit var userScoreUpdater: UserScoreUpdater

    @Test
    fun `'apply' will return OK when updateLiveMatches is successful`() {
        val result = userScoreUpdater.apply(Request())

        verify(userScoreUpdaterService).updateScores()

        assertThat(result.statusCode, `is`(OK.value()))
    }
}