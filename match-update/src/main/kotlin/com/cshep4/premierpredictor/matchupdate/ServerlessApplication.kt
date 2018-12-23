package com.cshep4.premierpredictor.matchupdate

import com.cshep4.premierpredictor.matchupdate.functions.MatchUpdate
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@SpringBootApplication
@EntityScan(basePackages = ["com.cshep4.premierpredictor.matchupdate.entity"])
@EnableJpaRepositories(basePackages = ["com.cshep4.premierpredictor.matchupdate.repository.sql"])
@EnableRedisRepositories(basePackages = ["com.cshep4.premierpredictor.matchupdate.repository.redis"])
@EnableDynamoDBRepositories(basePackages = ["com.cshep4.premierpredictor.matchupdate.repository.dynamodb"])
@Import(MatchUpdate::class)
class ServerlessApplication

fun main(args: Array<String>) {
    runApplication<ServerlessApplication>(*args)
}