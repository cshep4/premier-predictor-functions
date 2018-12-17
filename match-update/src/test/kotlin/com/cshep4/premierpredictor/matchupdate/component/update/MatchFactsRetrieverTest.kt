package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.ApiRequester
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
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
internal class MatchFactsRetrieverTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var matchFactsRetriever: MatchFactsRetriever

    @Test
    fun `'getLatest' will retrieve the match from the api`() {
        val now = LocalDateTime.now().plusDays(1)

        val apiResult = MatchFacts()
        val expectedResult = MatchFacts(lastUpdated = now).toSantisedMatchFacts()

        whenever(fixtureApiRequester.retrieveMatch("1")).thenReturn(apiResult)
        whenever(time.localDateTimeNow()).thenReturn(now)

        val result = matchFactsRetriever.getLatest("1")

        assertThat(result, `is`(expectedResult))
        verify(fixtureApiRequester).retrieveMatch("1")
        verify(time).localDateTimeNow()
    }

    @Test
    fun `'getLatest' will return null if nothing is retrieved from the api`() {
        whenever(fixtureApiRequester.retrieveMatch("1")).thenReturn(null)

        val result = matchFactsRetriever.getLatest("1")

        assertThat(result, `is`(nullValue()))
        verify(fixtureApiRequester).retrieveMatch("1")
        verify(time, times(0)).localDateTimeNow()
    }
}