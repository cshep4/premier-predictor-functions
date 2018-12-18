package com.cshep4.premierpredictor.matchdatarefresh.config

import com.sendgrid.SendGrid
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EmailConfig {
    @Value("\${SEND_GRID_API_KEY}")
    private val sendGridApiKey: String? = null

    @Bean
    fun sendGrid(): SendGrid {
        return SendGrid(sendGridApiKey)
    }
}