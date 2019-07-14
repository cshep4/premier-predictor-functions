package com.cshep4.premierpredictor.matchdatarefresh.utils

import com.mongodb.*
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.pojo.PojoCodecProvider

object TestUtils {
    fun mongoClient(): MongoClient {
        val pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder()
                                .automatic(true)
                                .build()
                )
        )

        val settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(ConnectionString("mongodb://localhost:27017"))
                .build()

        return MongoClients.create(settings)
    }
}