package com.cshep4.premierpredictor.matchdatarefresh.utils

object MatchFactUtils {
    fun sanitiseScore(score: String?): String? {
        if (score == null || score == "?") {
            return ""
        }

        return score
    }

    fun getFullTeamName(name: String?): String? = when (name) {
        "Bournemouth" -> "AFC Bournemouth"
        "Brighton" -> "Brighton & Hove Albion"
        "Cardiff" -> "Cardiff City"
        "Huddersfield" -> "Huddersfield Town"
        "Leicester" -> "Leicester City"
        "Newcastle" -> "Newcastle United"
        "Tottenham" -> "Tottenham Hotspur"
        "West Ham" -> "West Ham United"
        "Wolves" -> "Wolverhampton Wanderers"
        "Manchester Utd" -> "Manchester United"
        "Norwich" -> "Norwich City"
        "Sheffield" -> "Sheffield United"
        "Sheffield Utd" -> "Sheffield United"
        else -> name
    }

    fun correctStatus(status: String?): String? {
        if (status == null || ":" in status) {
            return null
        }

        return status
    }
}