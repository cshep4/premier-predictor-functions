package com.cshep4.premierpredictor.userscoreupdater.extensions

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime.isToday(): Boolean =
        this.toLocalDate() == LocalDate.now(Clock.systemUTC())

fun LocalDate.isToday(): Boolean =
        this == LocalDate.now(Clock.systemUTC())

