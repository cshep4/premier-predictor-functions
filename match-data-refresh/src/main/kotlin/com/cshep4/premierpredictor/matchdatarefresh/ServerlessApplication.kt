package com.cshep4.premierpredictor.matchdatarefresh

import com.cshep4.premierpredictor.matchdatarefresh.functions.MatchDataRefresh
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@EntityScan(basePackages = ["com.cshep4.premierpredictor.matchdatarefresh.entity"])
@EnableDynamoDBRepositories(basePackages = ["com.cshep4.premierpredictor.matchdatarefresh.repository"])
@Import(MatchDataRefresh::class)
class ServerlessApplication

fun main(args: Array<String>) {
    runApplication<ServerlessApplication>(*args)
}