package com.cshep4.premierpredictor.matchupdate.component.score

import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class ScoresUpdatedReaderTest {
    @Mock
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    @InjectMocks
    private lateinit var scoresUpdatedReader: ScoresUpdatedReader

    @Test
    fun `'scoresLastUpdated' retrieves date when scores last updated`() {
        whenever(scoresUpdatedRepository.findById(1)).thenReturn(Optional.of(ScoresUpdatedEntity(id = 1, lastUpdated = LocalDate.now())))

        val result = scoresUpdatedReader.scoresLastUpdated()

        assertThat(result, `is`(LocalDate.now()))
    }

    @Test
    fun `'scoresLastUpdated' returns 01-01-2000 if no date is stored`() {
        whenever(scoresUpdatedRepository.findById(1)).thenReturn(Optional.empty())

        val result = scoresUpdatedReader.scoresLastUpdated()

        assertThat(result, `is`(LocalDate.of(2000, 1, 1)))
    }
}