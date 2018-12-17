package com.cshep4.premierpredictor.matchupdate.functions

import com.cshep4.premierpredictor.matchupdate.domain.Request
import com.cshep4.premierpredictor.matchupdate.service.UpdateMatchService
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

@RunWith(MockitoJUnitRunner::class)
internal class MatchUpdateTest {
    @Mock
    private lateinit var updateMatchService: UpdateMatchService

    @InjectMocks
    private lateinit var matchUpdate: MatchUpdate

    @Test
    fun `'apply' will return OK when updateLiveMatches is successful`() {
        whenever(updateMatchService.updateLiveMatches()).thenReturn(true)

        val result = matchUpdate.apply(Request())

        assertThat(result.statusCode, `is`(OK.value()))
    }

    @Test
    fun `'apply' will return INTERNAL_SERVER_ERROR when updateLiveMatches is not successful`() {
        whenever(updateMatchService.updateLiveMatches()).thenReturn(false)

        val result = matchUpdate.apply(Request())

        assertThat(result.statusCode, `is`(INTERNAL_SERVER_ERROR.value()))
    }
}