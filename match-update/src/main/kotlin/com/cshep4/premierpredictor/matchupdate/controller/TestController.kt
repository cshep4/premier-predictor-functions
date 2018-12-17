package com.cshep4.premierpredictor.matchupdate.controller

import com.cshep4.premierpredictor.matchupdate.data.LiveMatch
import com.cshep4.premierpredictor.matchupdate.data.ScoresUpdated
import com.cshep4.premierpredictor.matchupdate.service.TestService
import com.cshep4.premierpredictor.matchupdate.service.UpdateMatchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*

@RestController
class TestController {
    @Autowired
    private lateinit var updateMatchService: UpdateMatchService

    @Autowired
    private lateinit var testService: TestService

    @GetMapping
    fun updateMatches() : ResponseEntity<Boolean> {
        return when (updateMatchService.updateLiveMatches()) {
            true -> ok().build()
            false -> status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{id}")
    fun getTestMatches(@PathVariable(value = "id") id: String) : ResponseEntity<LiveMatch?> {
        return ok().body(testService.getTestMatches(id))
    }

    @GetMapping("/all")
    fun getAllTestMatches() : ResponseEntity<List<LiveMatch>> {
        return ok().body(testService.getAllTestMatches())
    }

    @PostMapping
    fun addTestMatches(@RequestBody liveMatches: Set<LiveMatch>) : ResponseEntity<Boolean> {
        testService.addTestMatches(liveMatches)

        return ok().build()
    }

    @DeleteMapping("/{id}")
    fun removeTestMatches(@PathVariable(value = "id") id: String) : ResponseEntity<Boolean> {
        testService.removeTestMatches(id)

        return ok().build()
    }

    @PostMapping("/scores")
    fun setScoresUpdated(): ResponseEntity<ScoresUpdated> {
        return ok().body(testService.setScoresUpdated())
    }

    @GetMapping("/scores")
    fun getScoresUpdated(): ResponseEntity<ScoresUpdated> {
        return ok().body(testService.getScoresUpdated())
    }
}