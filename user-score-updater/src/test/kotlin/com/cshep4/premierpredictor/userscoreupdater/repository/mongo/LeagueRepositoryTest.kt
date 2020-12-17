package com.cshep4.premierpredictor.userscoreupdater.repository.mongo

import com.cshep4.premierpredictor.userscoreupdater.config.MongoConfig
import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import com.cshep4.premierpredictor.userscoreupdater.entity.LeagueUserEntity
import com.cshep4.premierpredictor.userscoreupdater.utils.TestUtils
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

internal class LeagueRepositoryTest {
    companion object {
        val ID_1 = ObjectId().toHexString()
        val ID_2 = ObjectId().toHexString()
    }

    private lateinit var leagueRepository: LeagueRepository

    private lateinit var mongoClient: MongoClient

    @Before
    fun init() {
        mongoClient = TestUtils.mongoClient()

        leagueRepository = LeagueRepository()
        ReflectionTestUtils.setField(leagueRepository, "client", mongoClient)
    }

    @Test
    fun `'save' will store a list of league users to the database, updating ones that already exist`() {
        leagueRepository.save(listOf(LeagueUser(
                id = ID_1,
                rank = 1,
                name = "first name 2 surname 2",
                predictedWinner = "2 winner",
                score = 8
        )))

        val leagueUser1 = LeagueUser(
                id = ID_1,
                rank = 1,
                name = "name 1",
                predictedWinner = "winner 1",
                score = 1
        )
        val leagueUser2 = LeagueUser(
                id = ID_2,
                rank = 2,
                name = "name 2",
                predictedWinner = "winner 2",
                score = 2
        )

        leagueRepository.save(listOf(leagueUser1, leagueUser2))

        val storedLeagueUser1 = mongoClient.getDatabase(LeagueRepository.DATABASE)
                .getCollection(LeagueRepository.COLLECTION, LeagueUserEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_1))
                .first()
                ?.toDto()!!

        assertThat(storedLeagueUser1, `is`(leagueUser1))
        assertThat(storedLeagueUser1.id, `is`(leagueUser1.id))
        assertThat(storedLeagueUser1.rank, `is`(leagueUser1.rank))
        assertThat(storedLeagueUser1.name, `is`(leagueUser1.name))
        assertThat(storedLeagueUser1.predictedWinner, `is`(leagueUser1.predictedWinner))
        assertThat(storedLeagueUser1.score, `is`(leagueUser1.score))

        val storedLeagueUser2 = mongoClient.getDatabase(LeagueRepository.DATABASE)
                .getCollection(LeagueRepository.COLLECTION, LeagueUserEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_2))
                .first()
                ?.toDto()!!

        assertThat(storedLeagueUser2, `is`(leagueUser2))
        assertThat(storedLeagueUser2.id, `is`(leagueUser2.id))
        assertThat(storedLeagueUser2.rank, `is`(leagueUser2.rank))
        assertThat(storedLeagueUser2.name, `is`(leagueUser2.name))
        assertThat(storedLeagueUser2.predictedWinner, `is`(leagueUser2.predictedWinner))
        assertThat(storedLeagueUser2.score, `is`(leagueUser2.score))
    }

    @After
    fun tearDown() {
        mongoClient.getDatabase(LeagueRepository.DATABASE)
                .drop()

        mongoClient.close()
    }
}