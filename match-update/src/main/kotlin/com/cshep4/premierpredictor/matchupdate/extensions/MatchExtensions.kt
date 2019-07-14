package com.cshep4.premierpredictor.matchupdate.extensions

import com.cshep4.premierpredictor.matchupdate.data.Match
import com.cshep4.premierpredictor.matchupdate.data.PredictedMatch
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts

fun List<PredictedMatch>.getMatchById(id: String?): PredictedMatch =
        first { it.id == id }

fun MatchFacts?.isPlaying(): Boolean =
        this?.status != null && status != "" && status != "FT" && status != "Postp." && status != "Cancl." && status != "Awarded" && status != "Aban." && ":" !in status!!

fun Match.hasPlayed(): Boolean = this.hGoals != null && this.aGoals != null