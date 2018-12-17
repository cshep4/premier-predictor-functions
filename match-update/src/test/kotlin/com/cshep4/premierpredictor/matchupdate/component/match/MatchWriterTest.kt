package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.FixturesRepository
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MatchWriterTest {
    @Mock
    private lateinit var fixturesRepository: FixturesRepository

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @InjectMocks
    private lateinit var matchWriter: MatchWriter

    @Test
    fun `'matches' saves matches to db`() {
        val matches = listOf(Match())
        val matchEntities = matches.map { MatchEntity.fromDto(it) }

        whenever(fixturesRepository.saveAll(matchEntities)).thenReturn(matchEntities)

        val result = matchWriter.matches(matches)

        assertThat(result, `is`(matches))
        verify(fixturesRepository).saveAll(matchEntities)
    }

    @Test
    fun `'matches' saves match facts to dynamo db`() {
        val matchFacts = listOf(MatchFacts())
        val matchFactsEntities = matchFacts.map { MatchFactsEntity.fromDto(it) }

        whenever(matchFactsRepository.saveAll(matchFactsEntities)).thenReturn(matchFactsEntities)

        val result = matchWriter.matchFacts(matchFacts)

        val expectedResult = matchFacts
                .map { MatchFactsEntity.fromDto(it) }
                .map { it.toDto() }

        assertThat(result, `is`(expectedResult))
        verify(matchFactsRepository).saveAll(matchFactsEntities)
    }
}