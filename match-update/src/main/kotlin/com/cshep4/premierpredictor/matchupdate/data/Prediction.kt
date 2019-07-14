package com.cshep4.premierpredictor.matchupdate.data

data class Prediction(
        var userId: String? = null,
        var matchId: String? = null,
        var hGoals: Int? = null,
        var aGoals: Int? = null
)