package com.cshep4.premierpredictor.userscoreupdater.data

import java.time.LocalDateTime

data class Match (
        val id: String = "",
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var played: Int = 0,
        var dateTime: LocalDateTime? = null,
        var matchday: Int = 0
) {
    fun toPredictedMatch(): PredictedMatch = PredictedMatch(
            id = this.id,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hResult = this.hGoals,
            aResult = this.aGoals,
            played = this.played,
            dateTime = this.dateTime,
            matchday = this.matchday)
}