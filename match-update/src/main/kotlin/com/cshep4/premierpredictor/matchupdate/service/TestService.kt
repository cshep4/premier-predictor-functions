package com.cshep4.premierpredictor.matchupdate.service

import com.cshep4.premierpredictor.matchupdate.component.match.MatchReader
import com.cshep4.premierpredictor.matchupdate.data.LiveMatch
import com.cshep4.premierpredictor.matchupdate.data.ScoresUpdated
import com.cshep4.premierpredictor.matchupdate.entity.LiveMatchEntity
import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.matchupdate.repository.redis.LiveMatchRepository
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TestService {
    @Autowired
    private lateinit var liveMatchRepository: LiveMatchRepository

    @Autowired
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    fun getTestMatches(id: String): LiveMatch? = liveMatchRepository.findById(id)
            .map { it.toDto() }
            .orElse(null)

    fun getAllTestMatches(): List<LiveMatch> = liveMatchRepository.findAll()
            .map { it.toDto() }

    fun addTestMatches(liveMatches: Set<LiveMatch>) {
        liveMatchRepository.saveAll(liveMatches.map { LiveMatchEntity.fromDto(it) })
    }

    fun getScoresUpdated(): ScoresUpdated? = scoresUpdatedRepository.findById(1)
            .map { it.toDto() }
            .orElse(null)

    fun setScoresUpdated(): ScoresUpdated = scoresUpdatedRepository.save(ScoresUpdatedEntity(id = 1, lastUpdated = LocalDate.now())).toDto()

    fun removeTestMatches(id: String) {
        liveMatchRepository.deleteById(id)
    }
}