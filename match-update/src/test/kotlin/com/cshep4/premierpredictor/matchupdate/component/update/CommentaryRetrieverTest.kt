package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.ApiRequester
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
internal class CommentaryRetrieverTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var commentaryRetriever: CommentaryRetriever

    @Test
    fun `'retrieveCommentaryFromApi' will retrieve the commentary from the api`() {
        val now = LocalDateTime.now().plusDays(1)

        val apiResult = Commentary()
        val expectedResult = Commentary(lastUpdated = now)

        whenever(fixtureApiRequester.retrieveCommentary("1")).thenReturn(apiResult)
        whenever(time.localDateTimeNow()).thenReturn(now)

        val result = commentaryRetriever.getLatest("1")

        assertThat(result, `is`(expectedResult))
        verify(fixtureApiRequester).retrieveCommentary("1")
        verify(time).localDateTimeNow()
    }

    @Test
    fun `'retrieveCommentaryFromApi' will return null if nothing is retrieved from the api`() {
        whenever(fixtureApiRequester.retrieveCommentary("1")).thenReturn(null)

        val result = commentaryRetriever.getLatest("1")

        assertThat(result, `is`(nullValue()))
        verify(fixtureApiRequester).retrieveCommentary("1")
        verify(time, times(0)).localDateTimeNow()
    }

}