package com.cshep4.premierpredictor.matchupdate.repository.mongo

import com.cshep4.premierpredictor.matchupdate.config.MongoConfig.Companion.ID
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
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
class LiveMatchServiceRepository {
    companion object {
        const val DATABASE = "liveMatch"
        const val COLLECTION = "liveMatch"
    }

    @Autowired
    private lateinit var client: MongoClient

    private fun database(): MongoDatabase {
        return client.getDatabase(DATABASE)
    }

    private fun collection(): MongoCollection<MatchFactsEntity> {
        return database().getCollection(COLLECTION, MatchFactsEntity::class.java)
    }

    fun findById(id: String): MatchFacts? {
        return collection()
                .find(eq(ID, id))
                .first()
                ?.toDto()
    }

    fun findAll(): List<MatchFacts> {
        return collection()
                .find()
                .map { it.toDto() }
                .toList()
    }

    fun save(fixture: MatchFacts) {
        val entity = MatchFactsEntity.fromDto(fixture)

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

    fun save(fixtures: List<MatchFacts>) {
        val opts = UpdateOptions()
        opts.upsert(true)

        val entities = fixtures.map {
            UpdateOneModel<MatchFactsEntity>(
                    eq(ID, it.id),
                    Document(
                            mapOf(
                                    Pair("\$set", MatchFactsEntity.fromDto(it))
                            )
                    ),
                    opts
            )
        }

        collection().bulkWrite(entities)
    }
}