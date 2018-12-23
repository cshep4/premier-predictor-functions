package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.OverrideMatch
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
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
internal class MatchUpdaterTest {
    @Mock
    private lateinit var matchWriter: MatchWriter

    @Mock
    private lateinit var overrideMatchRetriever: OverrideMatchRetriever

    @Mock
    private lateinit var matchOverrider: MatchOverrider

    @InjectMocks
    private lateinit var matchUpdater: MatchUpdater

    @Test
    fun `'update' returns list of matches when successfully updated to db`() {
        val matchFacts = listOf(MatchFacts(id = "1", localTeamName = "test1", visitorTeamName = "test2", week = "1"))
        matchFacts[0].setDateTime(LocalDateTime.now())

        val matches = matchFacts.map { it.toMatch() }
        val overrides = listOf(OverrideMatch())

        whenever(matchWriter.update(matches)).thenReturn(matches)
        whenever(overrideMatchRetriever.findAll()).thenReturn(overrides)
        whenever(matchOverrider.update(matches, overrides)).thenReturn(matches)

        val result = matchUpdater.update(matchFacts)

        verify(matchOverrider).update(matches, overrides)

        assertThat(result, `is`(matches))
    }

    @Test
    fun `'update' returns empty list when no matches found`() {
        val result = matchUpdater.update(emptyList())

        assertThat(result, `is`(emptyList()))
    }
}