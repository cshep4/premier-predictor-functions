package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.matchupdate.data.User
import com.cshep4.premierpredictor.matchupdate.entity.UserEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
    companion object {
        const val DATABASE = "user"
        const val COLLECTION = "user"
    }

    @Autowired
    private lateinit var client: MongoClient

    private fun database(): MongoDatabase {
        return client.getDatabase(DATABASE)
    }

    private fun collection(): MongoCollection<UserEntity> {
        return database().getCollection(COLLECTION, UserEntity::class.java)
    }


    fun findAll(): List<User> {
        return collection()
                .find()
                .map { it.toDto() }
                .toList()
    }

    fun save(users: List<User>) {
        val opts = UpdateOptions()
        opts.upsert(true)

        val entities = users.map {
            UpdateOneModel<UserEntity>(
                    Filters.eq(ID, ObjectId(it.id)),
                    Document(
                            mapOf(
                                    Pair("\$set", UserEntity.fromDto(it))
                            )
                    ),
                    opts
            )
        }

        collection().bulkWrite(entities)
    }
}