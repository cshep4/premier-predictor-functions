package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.component.api.ApiRequester
import com.cshep4.premierpredictor.matchdatarefresh.component.time.Time
import com.cshep4.premierpredictor.matchdatarefresh.repository.MatchFactsRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MatchUpdaterTest {
    @Mock
    private lateinit var fixtureApiRequester: ApiRequester

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Mock
    private lateinit var time: Time

    @InjectMocks
    private lateinit var matchUpdater: MatchUpdater

    @Test
    fun `'updateUpcomingMatchesWithLatestScores' `() {
    }
}