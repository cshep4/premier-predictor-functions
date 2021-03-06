package com.cshep4.premierpredictor.matchdatarefresh.repository

import com.cshep4.premierpredictor.matchdatarefresh.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class FixtureRepository {
    companion object {
        const val DATABASE = "fixture"
        const val COLLECTION = "2020-2021"
    }

    @Autowired
    private lateinit var client: MongoClient

    private fun database(): MongoDatabase {
        return client.getDatabase(DATABASE)
    }

    private fun collection(): MongoCollection<MatchEntity> {
        return database().getCollection(COLLECTION, MatchEntity::class.java)
    }

    fun save(fixture: Match) {
        val entity = MatchEntity.fromDto(fixture)

        val opts = FindOneAndUpdateOptions()
        opts.upsert(true)

        collection().findOneAndUpdate(
                eq(ID, entity.id),
                Document(
                        mapOf(
                                Pair("\$set", entity)
                        )
                ),
                opts
        )
    }

    fun save(fixtures: List<Match>) {
        val opts = UpdateOptions()
        opts.upsert(true)

        val entities = fixtures.map {
            UpdateOneModel<MatchEntity>(
                    eq(ID, it.id),
                    Document(
                            mapOf(
                                    Pair("\$set", MatchEntity.fromDto(it))
                            )
                    ),
                    opts
            )
        }

        collection().bulkWrite(entities)
    }
}