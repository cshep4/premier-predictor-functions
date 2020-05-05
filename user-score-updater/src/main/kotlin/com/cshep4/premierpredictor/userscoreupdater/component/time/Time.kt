package com.cshep4.premierpredictor.userscoreupdater.component.time

import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class Time {

    fun localDateNow(): LocalDate {
        return LocalDate.now(Clock.systemUTC())
    }
}