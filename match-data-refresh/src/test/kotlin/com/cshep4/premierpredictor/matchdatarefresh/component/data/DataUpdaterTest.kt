package com.cshep4.premierpredictor.matchdatarefresh.component.data

import com.cshep4.premierpredictor.matchdatarefresh.component.api.ApiRequester
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
import com.cshep4.premierpredictor.matchdatarefresh.component.matchfacts.MatchFactsUpdater
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class DataUpdaterTest {
    @Mock
    private lateinit var apiRequester: ApiRequester

    @Mock
    private lateinit var matchFactsUpdater: MatchFactsUpdater

    @Mock
    private lateinit var matchUpdater: MatchUpdater

    @InjectMocks
    private lateinit var dataUpdater: DataUpdater

    @Test
    fun `'matchData' will retrieve data from API and call relevent methods to update data`() {
        val matchFacts = listOf(MatchFacts())

        whenever(apiRequester.retrieveFixtures()).thenReturn(matchFacts)

        dataUpdater.matchData()

        verify(matchFactsUpdater).update(matchFacts)
        verify(matchUpdater).update(matchFacts)
    }
}
