package com.cshep4.premierpredictor.matchdatarefresh.entity

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.LocalDateTime

data class MatchEntity (
        @BsonProperty("_id")
        var id: String = "",
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var played: Int = 0,
        var dateTime: LocalDateTime? = null,
        var matchday: Int = 0
){
    fun toDto(): Match = Match(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hGoals,
            aGoals = this.aGoals,
            played = this.played,
            dateTime = this.dateTime,
            matchday = this.matchday)

    companion object {
        fun fromDto(dto: Match) = MatchEntity(
                id = dto.id,
                hTeam = dto.hTeam,
                aTeam = dto.aTeam,
                hGoals = dto.hGoals,
                aGoals = dto.aGoals,
                played = dto.played,
                dateTime = dto.dateTime,
                matchday = dto.matchday)
    }
}