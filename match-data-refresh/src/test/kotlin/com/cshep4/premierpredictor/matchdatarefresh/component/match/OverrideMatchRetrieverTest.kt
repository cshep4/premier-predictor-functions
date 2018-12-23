package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.OverrideMatch
import com.cshep4.premierpredictor.matchdatarefresh.entity.OverrideMatchEntity
import com.cshep4.premierpredictor.matchdatarefresh.repository.sql.OverrideMatchRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class OverrideMatchRetrieverTest {
    @Mock
    private lateinit var overrideMatchRepository: OverrideMatchRepository

    @InjectMocks
    private lateinit var overrideMatchRetriever: OverrideMatchRetriever

    @Test
    fun `'findAll' retrieves all matches where scores have been overridden and no others`() {
        val overriddenMatch1 = OverrideMatch(id = 1, hGoals = 2, aGoals = 0)
        val overriddenMatch2 = OverrideMatch(id = 1, hGoals = 2, aGoals = 0)
        val notOverriddenMatch = OverrideMatch(id = 1, hGoals = null, aGoals = null)

        val overriddenMatches = listOf(overriddenMatch1, overriddenMatch2)
        val overrideMatchRepoResult = listOf(
                OverrideMatchEntity.fromDto(overriddenMatch1),
                OverrideMatchEntity.fromDto(overriddenMatch2),
                OverrideMatchEntity.fromDto(notOverriddenMatch)
        )

        whenever(overrideMatchRepository.findAll()).thenReturn(overrideMatchRepoResult)

        val result = overrideMatchRetriever.findAll()

        MatcherAssert.assertThat(result, CoreMatchers.`is`(overriddenMatches))
    }
}