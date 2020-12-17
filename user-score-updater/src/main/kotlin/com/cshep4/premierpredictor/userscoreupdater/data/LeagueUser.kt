package com.cshep4.premierpredictor.userscoreupdater.data

data class LeagueUser(
        val id: String,
        val rank: Int,
        val name: String,
        val predictedWinner: String,
        val score: Int = 0
)