package com.cshep4.premierpredictor.matchdatarefresh.controller

import com.cshep4.premierpredictor.matchdatarefresh.service.MatchDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @Autowired
    private lateinit var matchDataService: MatchDataService

    @GetMapping
    fun updateMatches() : ResponseEntity<Boolean> {
        matchDataService.refresh()

        return ok().build()
    }
}