package com.cshep4.premierpredictor.userscoreupdater.entity

import com.cshep4.premierpredictor.userscoreupdater.data.ScoresUpdated
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
    }
}