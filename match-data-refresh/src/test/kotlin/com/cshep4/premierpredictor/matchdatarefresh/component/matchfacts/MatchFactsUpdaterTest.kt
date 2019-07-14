package com.cshep4.premierpredictor.matchdatarefresh.component.matchfacts

import com.cshep4.premierpredictor.matchdatarefresh.component.time.Time
import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.cshep4.premierpredictor.matchdatarefresh.repository.LiveMatchRepository
import com.nhaarman.mockito_kotlin.verify
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
internal class MatchFactsUpdaterTest {
    @Mock
    private lateinit var liveMatchRepository: LiveMatchRepository

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var matchFactsUpdater: MatchFactsUpdater

    @Test
    fun `'update' retrieves all matches from dynamoDB, filters out games not in future or yesterday and merges with latest version`() {
        val t = LocalDateTime.now()
        whenever(time.localDateTimeNow()).thenReturn(t)

        val apiResult = listOf(MatchFacts(id = "1"))
        apiResult[0].setDateTime(LocalDateTime.now().plusDays(1))

        val commentary = Commentary()
        val dbResult = listOf(MatchFacts(id = "1", commentary = commentary, lastUpdated = t))
        dbResult[0].formattedDate = apiResult[0].formattedDate
        dbResult[0].time = apiResult[0].time

        val dataToStore = listOf(MatchFacts(id = "1", lastUpdated = t))
        dataToStore[0].formattedDate = apiResult[0].formattedDate
        dataToStore[0].time = apiResult[0].time
        dataToStore[0].commentary = commentary

        whenever(liveMatchRepository.findAll()).thenReturn(dbResult)
        val result = matchFactsUpdater.update(apiResult)

        assertThat(result, `is`(dataToStore))
        verify(liveMatchRepository).save(dataToStore)
    }
}