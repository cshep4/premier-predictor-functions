package com.cshep4.premierpredictor.matchdatarefresh.functions

import com.cshep4.premierpredictor.matchdatarefresh.domain.Request
import com.cshep4.premierpredictor.matchdatarefresh.service.MatchDataService
import com.nhaarman.mockito_kotlin.verify
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
internal class MatchDataRefreshTest {
    @Mock
    private lateinit var matchDataService: MatchDataService

    @InjectMocks
    private lateinit var matchDataRefresh: MatchDataRefresh

    @Test
    fun `'apply' will refresh match data and return OK`() {
        val result = matchDataRefresh.apply(Request())

        verify(matchDataService).refresh()
        assertThat(result.statusCode, `is`(OK.value()))
    }
}