package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Lineup
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Position
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import com.cshep4.premierpredictor.matchupdate.entity.PredictionEntity
import com.cshep4.premierpredictor.matchupdate.repository.mongo.PredictionRepository.Companion.DATABASE
import com.cshep4.premierpredictor.matchupdate.repository.mongo.PredictionRepository.Companion.COLLECTION
import com.cshep4.premierpredictor.matchupdate.utils.TestUtils
import com.mongodb.client.MongoClient
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

internal class PredictionRepositoryTest {
    companion object {
        const val USER_ID_1 = "‍️🆔"
        const val USER_ID_2 = "🤙"
        const val MATCH_ID_1 = "‍️🏟"
        const val MATCH_ID_2 = "⚽️"
    }
    private lateinit var predictionRepository: PredictionRepository

    private lateinit var mongoClient: MongoClient

    @Before
    fun init() {
        mongoClient = TestUtils.mongoClient()

        predictionRepository = PredictionRepository()
        ReflectionTestUtils.setField(predictionRepository, "client", mongoClient)
    }

    @Test
    fun `'findByUserId' gets all predictions with userId from database`() {
        val predictionEntities1 = listOf(
                PredictionEntity(userId = USER_ID_1, matchId = MATCH_ID_1),
                PredictionEntity(userId = USER_ID_1, matchId = MATCH_ID_2)
        )

        mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, PredictionEntity::class.java)
                .insertMany(predictionEntities1)

        val predictionEntities2 = listOf(
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_1),
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_2)
        )

        mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, PredictionEntity::class.java)
                .insertMany(predictionEntities2)

        val result = predictionRepository.findByUserId(USER_ID_1)

        val expectedResult = predictionEntities1.map { it.toDto() }

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'findByUserId' returns empty list if no predictinos for that user exists`() {
        val predictionEntities2 = listOf(
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_1),
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_2)
        )

        mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, PredictionEntity::class.java)
                .insertMany(predictionEntities2)

        val result = predictionRepository.findByUserId(USER_ID_1)

        assertThat(result, `is`(emptyList()))
    }

    @Test
    fun `'findAll' gets all documents from database`() {
        val predictionEntities1 = listOf(
                PredictionEntity(userId = USER_ID_1, matchId = MATCH_ID_1),
                PredictionEntity(userId = USER_ID_1, matchId = MATCH_ID_2)
        )

        mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, PredictionEntity::class.java)
                .insertMany(predictionEntities1)

        val predictionEntities2 = listOf(
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_1),
                PredictionEntity(userId = USER_ID_2, matchId = MATCH_ID_2)
        )

        mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, PredictionEntity::class.java)
                .insertMany(predictionEntities2)

        val result = predictionRepository.findAll()

        val expectedResult = listOf(predictionEntities1, predictionEntities2)
                .flatten()
                .map { it.toDto() }

        assertThat(result, `is`(expectedResult))
    }

    @After
    fun tearDown() {
        mongoClient.getDatabase(DATABASE)
                .drop()

        mongoClient.close()
    }
}