package com.cshep4.premierpredictor.matchupdate.component.livematch

import com.cshep4.premierpredictor.matchupdate.component.update.CommentaryRetriever
import com.cshep4.premierpredictor.matchupdate.component.update.MatchFactsRetriever
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
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
import java.util.*

@RunWith(MockitoJUnitRunner::class)
internal class LiveMatchUpdaterTest {
    companion object {
        const val ID = "1"
        const val REFRESH_RATE = 60L
    }

    @Mock
    private lateinit var matchFactsRepository: MatchFactsRepository

    @Mock
    private lateinit var matchFactsRetriever: MatchFactsRetriever

    @Mock
    private lateinit var commentaryRetriever: CommentaryRetriever

    @InjectMocks
    private lateinit var liveMatchUpdater: LiveMatchUpdater

    @Test
    fun `'retrieveLatest' will update both match facts and commentary`() {
        val commentary = Commentary()
        val matchFacts = MatchFactsEntity(commentary = commentary)
        val updatedMatch = MatchFacts()
        val updatedCommentary = Commentary()

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchFactsRetriever.getLatest(ID)).thenReturn(updatedMatch)
        whenever(commentaryRetriever.getLatest(ID)).thenReturn(updatedCommentary)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(updatedMatch))
        assertThat(result!!.commentary, `is`(updatedCommentary))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLatest' will return current match facts if nothing returned from api`() {
        val commentary = Commentary()
        val matchFacts = MatchFactsEntity(commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchFactsRetriever.getLatest(ID)).thenReturn(null)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLatest' will return current commentary if nothing returned from api`() {
        val commentary = Commentary()
        val matchFacts = MatchFactsEntity(commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(commentaryRetriever.getLatest(ID)).thenReturn(null)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result!!))
    }

    @Test
    fun `'retrieveLatest' will return current match facts and commentary if nothing returned from api even if they need updating`() {
        val commentary = Commentary(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5))
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE + 5), commentary = commentary)

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(matchFactsRetriever.getLatest(ID)).thenReturn(null)
        whenever(commentaryRetriever.getLatest(ID)).thenReturn(null)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(matchFacts.toDto()))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLatest' will update the match facts if none are currently stored`() {
        val updatedMatch = MatchFacts()
        val commentary = Commentary()

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.empty())
        whenever(matchFactsRetriever.getLatest(ID)).thenReturn(updatedMatch)
        whenever(commentaryRetriever.getLatest(ID)).thenReturn(commentary)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(updatedMatch))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }

    @Test
    fun `'retrieveLatest' will return current commentary if none are currently stored`() {
        val matchFacts = MatchFactsEntity(lastUpdated = LocalDateTime.now().minusSeconds(REFRESH_RATE - 5), commentary = null)
        val commentary = Commentary()

        val expectedResult = matchFacts.toDto()
        expectedResult.commentary = commentary

        whenever(matchFactsRepository.findById(ID)).thenReturn(Optional.of(matchFacts))
        whenever(commentaryRetriever.getLatest(ID)).thenReturn(commentary)

        val result = liveMatchUpdater.retrieveLatest(ID)

        assertThat(result, `is`(expectedResult))
        assertThat(result!!.commentary, `is`(commentary))
        verify(matchFactsRetriever).getLatest(any())
        verify(commentaryRetriever).getLatest(any())
        verify(matchFactsRepository).save(MatchFactsEntity.fromDto(result))
    }
}