package com.cshep4.premierpredictor.userscoreupdater.repository.mongo

import com.cshep4.premierpredictor.userscoreupdater.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import com.cshep4.premierpredictor.userscoreupdater.entity.LeagueUserEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class LeagueRepository {
    companion object {
        const val DATABASE = "league"
        const val COLLECTION = "overall"
    }

    @Autowired
    private lateinit var client: MongoClient

    private fun database(): MongoDatabase {
        return client.getDatabase(DATABASE)
    }

    private fun collection(): MongoCollection<LeagueUserEntity> {
        return database().getCollection(COLLECTION, LeagueUserEntity::class.java)
    }

    fun save(users: List<LeagueUser>) {
        val opts = UpdateOptions()
        opts.upsert(true)

        val entities = users.map {
            UpdateOneModel<LeagueUserEntity>(
                    eq(ID, it.id),
                    Document(
                            mapOf(
                                    Pair("\$set", LeagueUserEntity.fromDto(it))
                            )
                    ),
                    opts
            )
        }

        collection().bulkWrite(entities)
    }
}