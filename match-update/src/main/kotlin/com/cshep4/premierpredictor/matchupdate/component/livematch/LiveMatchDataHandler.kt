package com.cshep4.premierpredictor.matchupdate.component.livematch

import com.cshep4.premierpredictor.matchupdate.repository.redis.LiveMatchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LiveMatchDataHandler {
    @Autowired
    private lateinit var liveMatchRepository: LiveMatchRepository

    fun retrieveMatchIds(): Set<String> = liveMatchRepository.findAll()
                .map { it.id }
                .toSet()

    fun remove(ids: List<String>) {
        ids.forEach { liveMatchRepository.deleteById(it) }
    }
}