package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository.Companion.COLLECTION
import com.cshep4.premierpredictor.matchupdate.repository.mongo.FixtureRepository.Companion.DATABASE
import com.cshep4.premierpredictor.matchupdate.utils.TestUtils
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters.eq
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

internal class FixtureRepositoryTest {
    companion object {
        const val ID_1 = "⚽️"
        const val ID_2 = "🏆"
    }
    private lateinit var fixtureRepository: FixtureRepository

    private lateinit var mongoClient: MongoClient

    @Before
    fun init() {
        mongoClient = TestUtils.mongoClient()

        fixtureRepository = FixtureRepository()
        ReflectionTestUtils.setField(fixtureRepository, "client", mongoClient)
    }

    @Test
    fun `'save' will store a fixture to the database`() {
        val fixture = Match(id = ID_1)

        fixtureRepository.save(fixture)

        val storedFixture = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchEntity::class.java)
                .find(eq(ID, ID_1))
                .first()
                ?.toDto()

        assertThat(storedFixture, `is`(fixture))
    }

    @Test
    fun `'save' will update a fixture in the database if it exists`() {
        fixtureRepository.save(Match(id = ID_1))

        val fixture = Match(id = ID_1, hGoals = 1, aGoals = 1)

        fixtureRepository.save(fixture)

        val storedFixture = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchEntity::class.java)
                .find(eq(ID, ID_1))
                .first()
                ?.toDto()!!

        assertThat(storedFixture, `is`(fixture))
        assertThat(storedFixture.hGoals, `is`(fixture.hGoals))
        assertThat(storedFixture.aGoals, `is`(fixture.aGoals))
    }

    @Test
    fun `'save' will store a list of fixtures to the database, updating ones that already exist`() {
        fixtureRepository.save(Match(id = ID_1))

        val fixture1 = Match(id = ID_1, hGoals = 1, aGoals = 1)
        val fixture2 = Match(id = ID_2, hGoals = 3, aGoals = 3)

        fixtureRepository.save(listOf(fixture1, fixture2))

        val storedFixture1 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchEntity::class.java)
                .find(eq(ID, ID_1))
                .first()
                ?.toDto()!!

        assertThat(storedFixture1, `is`(fixture1))
        assertThat(storedFixture1.hGoals, `is`(fixture1.hGoals))
        assertThat(storedFixture1.aGoals, `is`(fixture1.aGoals))

        val storedFixture2 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchEntity::class.java)
                .find(eq(ID, ID_2))
                .first()
                ?.toDto()!!

        assertThat(storedFixture2, `is`(fixture2))
        assertThat(storedFixture2.hGoals, `is`(fixture2.hGoals))
        assertThat(storedFixture2.aGoals, `is`(fixture2.aGoals))
    }

    @After
    fun tearDown() {
        mongoClient.getDatabase(DATABASE)
                .drop()

        mongoClient.close()
    }
}