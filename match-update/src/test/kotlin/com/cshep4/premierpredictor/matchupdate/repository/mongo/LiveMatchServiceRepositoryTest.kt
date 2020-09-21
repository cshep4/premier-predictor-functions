package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.config.MongoConfig
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Lineup
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Position
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository.Companion.COLLECTION
import com.cshep4.premierpredictor.matchupdate.repository.mongo.LiveMatchServiceRepository.Companion.DATABASE
import com.cshep4.premierpredictor.matchupdate.utils.TestUtils
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

internal class LiveMatchServiceRepositoryTest {
    companion object {
        const val ID_1 = "⚽️"
        const val ID_2 = "🏆"
    }
    private lateinit var liveMatchServiceRepository: LiveMatchServiceRepository

    private lateinit var mongoClient: MongoClient

    @Before
    fun init() {
        mongoClient = TestUtils.mongoClient()

        liveMatchServiceRepository = LiveMatchServiceRepository()
        ReflectionTestUtils.setField(liveMatchServiceRepository, "client", mongoClient)
    }

    @Test
    fun `'findById' gets documents with id from database`() {
        val commentary = Commentary(matchId = ID_1, lineup = Lineup(localTeam = listOf(Position()), visitorTeam = listOf(Position())))
        val match = MatchFacts(id = ID_1, commentary = commentary, localTeamScore = "1", visitorTeamScore = "1")
        liveMatchServiceRepository.save(match)

        val result = liveMatchServiceRepository.findById(ID_1)

        assertThat(result, `is`(match))
    }

    @Test
    fun `'findById' returns null if document does not exist`() {
        val commentary = Commentary(matchId = ID_1, lineup = Lineup(localTeam = listOf(Position()), visitorTeam = listOf(Position())))
        val match = MatchFacts(id = ID_1, commentary = commentary, localTeamScore = "1", visitorTeamScore = "1")
        liveMatchServiceRepository.save(match)

        val result = liveMatchServiceRepository.findById(ID_2)

        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `'findAll' gets all documents from database`() {
        val commentary = Commentary(matchId = ID_1, lineup = Lineup(localTeam = listOf(Position()), visitorTeam = listOf(Position())))
        val match1 = MatchFacts(id = ID_1, commentary = commentary, localTeamScore = "1", visitorTeamScore = "1")
        liveMatchServiceRepository.save(match1)

        val match2 = MatchFacts(id = ID_2, commentary = commentary, localTeamScore = "1", visitorTeamScore = "1", formattedDate = "01.02.2019")
        liveMatchServiceRepository.save(match2)

        val result = liveMatchServiceRepository.findAll()

        assertThat(result[0], `is`(match1))
        assertThat(result[1], `is`(match2))
    }

    @Test
    fun `'save' will store a match to the database`() {
        val commentary = Commentary(matchId = ID_1, lineup = Lineup(localTeam = listOf(Position()), visitorTeam = listOf(Position())))
        val match = MatchFacts(id = ID_1, commentary = commentary, localTeamScore = "1", visitorTeamScore = "1")

        liveMatchServiceRepository.save(match)

        val storedMatch = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchFactsEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_1))
                .first()
                ?.toDto()

        assertThat(storedMatch, `is`(match))
    }

    @Test
    fun `'save' will update a match in the database if it exists`() {
        val commentary = Commentary(matchId = ID_1, lineup = Lineup(localTeam = listOf(Position()), visitorTeam = listOf(Position())))
        val match = MatchFacts(id = ID_1, commentary = commentary)

        liveMatchServiceRepository.save(match)

        match.localTeamScore = "2"
        match.visitorTeamScore = "1"

        liveMatchServiceRepository.save(match)

        val storedMatch = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchFactsEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_1))
                .first()
                ?.toDto()!!

        assertThat(storedMatch, `is`(match))
        assertThat(storedMatch.localTeamScore, `is`(match.localTeamScore))
        assertThat(storedMatch.visitorTeamScore, `is`(match.visitorTeamScore))
    }

    @Test
    fun `'save' will store a list of matches to the database, updating ones that already exist`() {
        liveMatchServiceRepository.save(MatchFacts(id = ID_1))

        val match1 = MatchFacts(id = ID_1, localTeamScore = "1", visitorTeamScore = "1")
        val match2 = MatchFacts(id = ID_2, localTeamScore = "3", visitorTeamScore = "3")

        liveMatchServiceRepository.save(listOf(match1, match2))

        val storedMatch1 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchFactsEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_1))
                .first()
                ?.toDto()!!

        assertThat(storedMatch1, `is`(match1))
        assertThat(storedMatch1.localTeamScore, `is`(match1.localTeamScore))
        assertThat(storedMatch1.visitorTeamScore, `is`(match1.visitorTeamScore))

        val storedMatch2 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, MatchFactsEntity::class.java)
                .find(Filters.eq(MongoConfig.ID, ID_2))
                .first()
                ?.toDto()!!

        assertThat(storedMatch2, `is`(match2))
        assertThat(storedMatch2.localTeamScore, `is`(match2.localTeamScore))
        assertThat(storedMatch2.visitorTeamScore, `is`(match2.visitorTeamScore))
    }

    @After
    fun tearDown() {
        mongoClient.getDatabase(DATABASE)
                .drop()

        mongoClient.close()
    }
}