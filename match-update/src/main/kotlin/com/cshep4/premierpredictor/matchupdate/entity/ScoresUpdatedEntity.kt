package com.cshep4.premierpredictor.matchupdate.entity

import com.cshep4.premierpredictor.matchupdate.data.ScoresUpdated
import org.springframework.data.redis.core.RedisHash
import java.time.Clock
import java.time.LocalDate

@RedisHash("ScoresUpdated")
data class ScoresUpdatedEntity(
        var id: Int = 1,
        var lastUpdated: LocalDate = LocalDate.now(Clock.systemUTC())
){
    fun toDto(): ScoresUpdated = ScoresUpdated(
            id = this.id,
            lastUpdated = this.lastUpdated
    )

    companion object {
        fun fromDto(dto: ScoresUpdated) = ScoresUpdatedEntity(
                id = dto.id,
                lastUpdated = dto.lastUpdated
        )
    }
}