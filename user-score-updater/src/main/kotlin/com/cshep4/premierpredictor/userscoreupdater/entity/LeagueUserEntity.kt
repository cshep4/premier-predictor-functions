package com.cshep4.premierpredictor.userscoreupdater.entity

import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import com.cshep4.premierpredictor.userscoreupdater.extensions.toObjectId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

data class LeagueUserEntity(
        @BsonProperty("_id")
        var id: ObjectId,
        val rank: Int,
        val name: String,
        val predictedWinner: String,
        val score: Int = 0
) {

    fun toDto(): LeagueUser = LeagueUser(
            id = this.id.toHexString(),
            rank = this.rank,
            name = this.name,
            predictedWinner = this.predictedWinner,
            score = this.score
    )

    companion object {
        fun fromDto(dto: LeagueUser): LeagueUserEntity = LeagueUserEntity(
                id = dto.id.toObjectId(),
                rank = dto.rank,
                name = dto.name,
                predictedWinner = dto.predictedWinner,
                score = dto.score
        )
    }
}