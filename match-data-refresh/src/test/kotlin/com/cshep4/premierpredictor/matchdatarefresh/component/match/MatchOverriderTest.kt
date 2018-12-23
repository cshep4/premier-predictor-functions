package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.data.OverrideMatch
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

internal class MatchOverriderTest {
    private var matchOverrider = MatchOverrider()

    @Test
    fun `'update' takes a list of matches and overridden scores and merges them to create a list of matches`() {
        val matches = listOf(
                Match(id = 1, hGoals = 1, aGoals = 0),
                Match(id = 2, hGoals = 4, aGoals = 1),
                Match(id = 3, hGoals = null, aGoals = null),
                Match(id = 4, hGoals = null, aGoals = null)
        )

        val overrides = listOf(
                OverrideMatch(id = 1, hGoals = 3, aGoals = 3),
                OverrideMatch(id = 3, hGoals = 2, aGoals = 0)
        )

        val result = matchOverrider.update(matches, overrides)

        MatcherAssert.assertThat(result[0].id, CoreMatchers.`is`(1L))
        MatcherAssert.assertThat(result[0].hGoals, CoreMatchers.`is`(3))
        MatcherAssert.assertThat(result[0].aGoals, CoreMatchers.`is`(3))

        MatcherAssert.assertThat(result[1].id, CoreMatchers.`is`(2L))
        MatcherAssert.assertThat(result[1].hGoals, CoreMatchers.`is`(4))
        MatcherAssert.assertThat(result[1].aGoals, CoreMatchers.`is`(1))

        MatcherAssert.assertThat(result[2].id, CoreMatchers.`is`(3L))
        MatcherAssert.assertThat(result[2].hGoals, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(result[2].aGoals, CoreMatchers.`is`(0))

        MatcherAssert.assertThat(result[3].id, CoreMatchers.`is`(4L))
        MatcherAssert.assertThat(result[3].hGoals, CoreMatchers.`is`(CoreMatchers.nullValue()))
        MatcherAssert.assertThat(result[3].aGoals, CoreMatchers.`is`(CoreMatchers.nullValue()))
    }
}