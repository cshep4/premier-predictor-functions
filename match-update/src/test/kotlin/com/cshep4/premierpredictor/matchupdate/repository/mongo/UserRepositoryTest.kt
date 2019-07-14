package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.matchupdate.data.User
import com.cshep4.premierpredictor.matchupdate.entity.UserEntity
import com.cshep4.premierpredictor.matchupdate.repository.mongo.UserRepository.Companion.COLLECTION
import com.cshep4.premierpredictor.matchupdate.repository.mongo.UserRepository.Companion.DATABASE
import com.cshep4.premierpredictor.matchupdate.utils.TestUtils
import com.mongodb.client.MongoClient
import com.mongodb.client.model.Filters.eq
import org.bson.types.ObjectId
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils

internal class UserRepositoryTest {
    companion object {
        val ID_1 = ObjectId().toHexString()
        val ID_2 = ObjectId().toHexString()
        val ID_3 = ObjectId().toHexString()
        val ID_4 = ObjectId().toHexString()

        const val EMAIL_1 = "✉️"
        const val EMAIL_2 = "📧"
        const val EMAIL_3 = "📨"
        const val EMAIL_4 = "💌"
    }
    private lateinit var userRepository: UserRepository

    private lateinit var mongoClient: MongoClient

    @Before
    fun init() {
        mongoClient = TestUtils.mongoClient()

        userRepository = UserRepository()
        ReflectionTestUtils.setField(userRepository, "client", mongoClient)
    }

    @Test
    fun `'findAll' gets all documents from database`() {
        val users1 = listOf(User(id = ID_1, email = EMAIL_1), User(id = ID_2, email = EMAIL_2))
        userRepository.save(users1)

        val users2 = listOf(User(id = ID_3, email = EMAIL_3), User(id = ID_4, email = EMAIL_4))
        userRepository.save(users2)

        val result = userRepository.findAll()

        val expectedResult = listOf(users1, users2)
                .flatten()

        assertThat(result, `is`(expectedResult))
    }

    @Test
    fun `'save' will store a list of users to the database, updating ones that already exist`() {
        userRepository.save(listOf(User(id = ID_1, email = EMAIL_1, firstName = "😁")))

        val user1 = User(id = ID_1, email = EMAIL_1, firstName = "😁", score = 10)
        val user2 = User(id = ID_2, email = EMAIL_2, firstName = "😥", score = 5)

        userRepository.save(listOf(user1, user2))

        val storedUser1 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, UserEntity::class.java)
                .find(eq(ID, ObjectId(ID_1)))
                .first()
                ?.toDto()!!

        assertThat(storedUser1, `is`(user1))
        assertThat(storedUser1.firstName, `is`(user1.firstName))
        assertThat(storedUser1.score, `is`(user1.score))

        val storedUser2 = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, UserEntity::class.java)
                .find(eq(ID, ObjectId(ID_2)))
                .first()
                ?.toDto()!!

        assertThat(storedUser2, `is`(user2))
        assertThat(storedUser2.firstName, `is`(user2.firstName))
        assertThat(storedUser2.score, `is`(user2.score))

        val allStoredUsers = mongoClient.getDatabase(DATABASE)
                .getCollection(COLLECTION, UserEntity::class.java)
                .find()
                .toList()

        assertThat(allStoredUsers.size, `is`(2))
    }

    @After
    fun tearDown() {
        mongoClient.getDatabase(DATABASE)
                .drop()

        mongoClient.close()
    }
}