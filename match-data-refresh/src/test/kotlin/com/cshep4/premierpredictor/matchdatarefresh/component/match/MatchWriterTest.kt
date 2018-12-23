package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchEntity
import com.cshep4.premierpredictor.matchdatarefresh.repository.sql.FixturesRepository
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
    private lateinit var fixturesRepository: FixturesRepository

    @InjectMocks
    private lateinit var matchWriter: MatchWriter

    @Test
    fun `'update' saves all matches and returns list of matches`() {
        val matches = listOf(Match(), Match(), Match(), Match())
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        whenever(fixturesRepository.saveAll(matchEntities)).thenReturn(matchEntities)

        val result = matchWriter.update(matches)

        MatcherAssert.assertThat(result, CoreMatchers.`is`(matches))
        verify(fixturesRepository).saveAll(matchEntities)
    }

    @Test
    fun `'update' does not save if no matches and returns empty list`() {
        val result = matchWriter.update(emptyList())

        MatcherAssert.assertThat(result, CoreMatchers.`is`(emptyList()))
        verify(fixturesRepository, times(0)).saveAll(emptyList())
    }
}