package com.cshep4.premierpredictor.matchupdate.component.time

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Component
class Time {
    fun localDateTimeNow(): LocalDateTime {
        return LocalDateTime.now(Clock.systemUTC())
    }

    fun localDateNow(): LocalDate {
        return LocalDate.now(Clock.systemUTC())
    }
}