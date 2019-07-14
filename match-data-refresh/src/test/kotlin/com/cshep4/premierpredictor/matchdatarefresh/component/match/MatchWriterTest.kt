package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.repository.FixtureRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MatchWriterTest {
    @Mock
    private lateinit var fixtureRepository: FixtureRepository

    @InjectMocks
    private lateinit var matchWriter: MatchWriter

    @Test
    fun `'update' saves all matches and returns list of matches`() {
        val matches = listOf(Match(), Match(), Match(), Match())
        val result = matchWriter.update(matches)

        MatcherAssert.assertThat(result, CoreMatchers.`is`(matches))

        verify(fixtureRepository).save(matches)
    }

    @Test
    fun `'update' does not save if no matches and returns empty list`() {
        val result = matchWriter.update(emptyList())

        MatcherAssert.assertThat(result, CoreMatchers.`is`(emptyList()))
        verify(fixtureRepository, times(0)).save(any<List<Match>>())
    }
}