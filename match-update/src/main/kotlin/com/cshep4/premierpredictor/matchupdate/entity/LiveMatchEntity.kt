package com.cshep4.premierpredictor.matchupdate.entity

import com.cshep4.premierpredictor.matchupdate.data.LiveMatch
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("LiveMatch")
data class LiveMatchEntity(
        @Id
        val id: String = "",
        var hTeam: String = "",
        var aTeam: String = "",
        var formattedDate: String = ""
){
    fun toDto(): LiveMatch = LiveMatch(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            formattedDate = this.formattedDate
    )

    companion object {
        fun fromDto(dto: LiveMatch) = LiveMatchEntity(
                id = dto.id,
                hTeam = dto.hTeam,
                aTeam = dto.aTeam,
                formattedDate = dto.formattedDate
        )
    }
}