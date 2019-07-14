package com.cshep4.premierpredictor.matchdatarefresh

import com.cshep4.premierpredictor.matchdatarefresh.functions.MatchDataRefresh
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(MatchDataRefresh::class)
class ServerlessApplication

fun main(args: Array<String>) {
    runApplication<ServerlessApplication>(*args)
}