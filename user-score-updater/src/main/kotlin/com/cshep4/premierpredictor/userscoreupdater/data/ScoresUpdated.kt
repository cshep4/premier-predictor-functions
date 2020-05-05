package com.cshep4.premierpredictor.userscoreupdater.data

import java.time.Clock
import java.time.LocalDate

data class ScoresUpdated(
        val id: Int = 1,
        var lastUpdated: LocalDate = LocalDate.now(Clock.systemUTC())
)