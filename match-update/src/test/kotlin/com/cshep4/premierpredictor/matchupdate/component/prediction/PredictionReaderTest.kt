package com.cshep4.premierpredictor.matchupdate.component.prediction

import com.cshep4.premierpredictor.matchupdate.entity.PredictionEntity
import com.cshep4.premierpredictor.matchupdate.repository.sql.PredictionsRepository
import com.nhaarman.mockito_kotlin.whenever
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class PredictionReaderTest {
    @Mock
    private lateinit var predictionsRepository: PredictionsRepository

    @InjectMocks
    private lateinit var predictionReader: PredictionReader

    @Test
    fun `'retrievePredictionsByUserId' should retrieve all predictions for that user`() {
        val predictionEntity = PredictionEntity()
        val predictions = listOf(predictionEntity)
        whenever(predictionsRepository.findByUserId(1)).thenReturn(predictions)

        val result = predictionReader.retrievePredictionsByUserId(1)

        MatcherAssert.assertThat(result.isEmpty(), CoreMatchers.`is`(false))
        MatcherAssert.assertThat(result[0], CoreMatchers.`is`(predictionEntity.toDto()))
    }

    @Test
    fun `'retrievePredictionsByUserId' should return empty list if no predictions exist for that user id`() {
        whenever(predictionsRepository.findByUserId(1)).thenReturn(emptyList())

        val result = predictionReader.retrievePredictionsByUserId(1)

        MatcherAssert.assertThat(result.isEmpty(), CoreMatchers.`is`(true))
    }
}