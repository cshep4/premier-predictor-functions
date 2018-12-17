package com.cshep4.premierpredictor.matchupdate.component.score

import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ScoresUpdatedReader {
    @Autowired
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    fun scoresLastUpdated(): LocalDate = scoresUpdatedRepository.findById(1)
            .map { it.lastUpdated }
            .orElse(LocalDate.of(2000, 1, 1))
}