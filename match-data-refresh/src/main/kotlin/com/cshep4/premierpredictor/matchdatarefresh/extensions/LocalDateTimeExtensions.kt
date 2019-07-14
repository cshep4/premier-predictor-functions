package com.cshep4.premierpredictor.matchdatarefresh.extensions

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime.isInFuture(): Boolean =
        this.isAfter(LocalDateTime.now(Clock.systemUTC()))

fun LocalDateTime.isYesterday(): Boolean =
        this.toLocalDate() == LocalDate.now(Clock.systemUTC()).minusDays(1)

fun LocalDateTime.isToday(): Boolean =
        this.toLocalDate() == LocalDate.now(Clock.systemUTC())