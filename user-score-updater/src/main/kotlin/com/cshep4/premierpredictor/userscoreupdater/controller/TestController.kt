package com.cshep4.premierpredictor.userscoreupdater.controller

import com.cshep4.premierpredictor.userscoreupdater.service.UserScoreUpdaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @Autowired
    private lateinit var userScoreUpdaterService: UserScoreUpdaterService

    @GetMapping
    fun updateMatches(): ResponseEntity<Boolean> {
        userScoreUpdaterService.updateScores()
        return ok().build()
    }
}