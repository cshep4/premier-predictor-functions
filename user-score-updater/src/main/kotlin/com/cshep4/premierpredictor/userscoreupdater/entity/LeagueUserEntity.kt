package com.cshep4.premierpredictor.userscoreupdater.entity

import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import org.bson.types.ObjectId
import org.bson.types.ObjectId.isValid

data class LeagueUserEntity(
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
        fun fromDto(dto: LeagueUser): LeagueUserEntity {
            if (!isValid(dto.id)) {
                throw IllegalArgumentException("Invalid id")
            }

            return LeagueUserEntity(
                    id = ObjectId(dto.id),
                    rank = dto.rank,
                    name = dto.name,
                    predictedWinner = dto.predictedWinner,
                    score = dto.score
            )
        }
    }
}