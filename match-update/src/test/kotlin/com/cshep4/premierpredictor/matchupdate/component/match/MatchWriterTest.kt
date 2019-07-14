package com.cshep4.premierpredictor.matchupdate.component.match

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchRepository
import com.nhaarman.mockito_kotlin.verify
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
    private lateinit var fixtureRepository: FixtureRepository

    @Mock
    private lateinit var liveMatchRepository: LiveMatchRepository

    @InjectMocks
    private lateinit var matchWriter: MatchWriter

    @Test
    fun `'fixtures' saves matches to db`() {
        val fixtures = listOf(Match())

        val result = matchWriter.fixtures(fixtures)

        assertThat(result, `is`(fixtures))
        verify(fixtureRepository).save(fixtures)
    }

    @Test
    fun `'matchFacts' saves match facts to db`() {
        val matchFacts = listOf(MatchFacts())

        val result = matchWriter.matchFacts(matchFacts)

        assertThat(result, `is`(matchFacts))
        verify(liveMatchRepository).save(matchFacts)
    }
}