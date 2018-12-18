package com.cshep4.premierpredictor.matchdatarefresh.component.time

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class Time {
    fun localDateTimeNow(): LocalDateTime {
        return LocalDateTime.now(Clock.systemUTC())
    }
}