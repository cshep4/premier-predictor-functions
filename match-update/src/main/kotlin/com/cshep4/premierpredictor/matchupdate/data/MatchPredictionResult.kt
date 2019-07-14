package com.cshep4.premierpredictor.matchupdate.data

data class MatchPredictionResult (
        val userId: String = "",
        var matchId: String = "",
        var hTeam: String = "",
        var aTeam: String = "",
        var hGoals: Int? = null,
        var aGoals: Int? = null,
        var hPredictedGoals: Int? = null,
        var aPredictedGoals: Int? = null,
        var matchday: Int = 0
) {
    fun toPredictedMatch(): Match = Match(
            id = this.matchId,
            hTeam = this.hTeam,
            aTeam = this.aTeam,
            hGoals = this.hPredictedGoals,
            aGoals = this.aPredictedGoals,
            matchday = this.matchday)
}