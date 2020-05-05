package com.cshep4.premierpredictor.userscoreupdater

import com.cshep4.premierpredictor.userscoreupdater.functions.UserScoreUpdater
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.cshep4.premierpredictor.userscoreupdater.entity"])
@EnableRedisRepositories(basePackages = ["com.cshep4.premierpredictor.userscoreupdater.repository.redis"])
@Import(UserScoreUpdater::class)
class ServerlessApplication

fun main(args: Array<String>) {
    runApplication<ServerlessApplication>(*args)
}