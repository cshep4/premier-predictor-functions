package com.cshep4.premierpredictor.userscoreupdater.repository.mongo

import com.cshep4.premierpredictor.userscoreupdater.data.Prediction
import com.cshep4.premierpredictor.userscoreupdater.entity.PredictionEntity
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class PredictionRepository {
    companion object {
        const val DATABASE = "prediction"
        const val COLLECTION = "prediction"
        const val USER_ID = "userId"
    }

    @Autowired
    private lateinit var client: MongoClient

    private fun database(): MongoDatabase {
        return client.getDatabase(DATABASE)
    }

    private fun collection(): MongoCollection<PredictionEntity> {
        return database().getCollection(COLLECTION, PredictionEntity::class.java)
    }

    fun findByUserId(id: String): List<Prediction> {
        return collection()
                .find(eq(USER_ID, id))
                .map { it.toDto() }
                .toList()
    }

    fun findAll(): List<Prediction> {
        return collection()
                .find()
                .map { it.toDto() }
                .toList()
    }
}