package com.cshep4.premierpredictor.matchdatarefresh.extensions

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

fun LocalDateTime.isInFuture(): Boolean =
        this.isAfter(LocalDateTime.now(Clock.systemUTC()))