package com.cshep4.premierpredictor.userscoreupdater.config

import com.cshep4.premierpredictor.userscoreupdater.entity.ScoresUpdatedEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisConfig {
    @Value("\${REDIS_HOST}")
    private val redisHost = ""

    @Value("\${REDIS_PORT}")
    private val redisPort = ""

    @Value("\${REDIS_PASSWORD}")
    private val redisPassword = ""

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration()
        redisConfig.hostName = redisHost
        redisConfig.port = redisPort.toInt()
        redisConfig.password = RedisPassword.of(redisPassword)

        return JedisConnectionFactory(redisConfig)
    }

    @Bean
    fun redisScoreTemplate(): RedisTemplate<Long, ScoresUpdatedEntity> {
        val template = RedisTemplate<Long, ScoresUpdatedEntity>()
        template.setConnectionFactory(jedisConnectionFactory())
        return template
    }
}