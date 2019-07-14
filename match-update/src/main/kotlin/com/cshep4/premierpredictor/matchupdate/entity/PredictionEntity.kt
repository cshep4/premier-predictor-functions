package com.cshep4.premierpredictor.matchupdate.entity

import com.cshep4.premierpredictor.matchupdate.data.Prediction

data class PredictionEntity (
        var userId: String? = null,
        var matchId: String? = null,
        var hGoals: Int? = null,
        var aGoals: Int? = null
){
    fun toDto(): Prediction = Prediction(
            userId = this.userId,
            matchId = this.matchId,
            aGoals = this.aGoals,
            hGoals = this.hGoals)

    companion object {
        fun fromDto(dto: Prediction) = PredictionEntity(
                userId = dto.userId,
                matchId = dto.matchId,
                hGoals = dto.hGoals,
                aGoals = dto.aGoals)
    }
}