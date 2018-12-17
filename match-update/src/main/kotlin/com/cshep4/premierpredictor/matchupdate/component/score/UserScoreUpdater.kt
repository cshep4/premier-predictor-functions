package com.cshep4.premierpredictor.matchupdate.component.score

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionCleaner
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.User
import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.matchupdate.entity.UserEntity
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.PredictedMatchRepository
import com.cshep4.premierpredictor.matchupdate.repository.sql.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Service
class UserScoreUpdater {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var predictedMatchRepository: PredictedMatchRepository

    @Autowired
    private lateinit var leagueTableScoreCalculator: LeagueTableScoreCalculator

    @Autowired
    private lateinit var matchScoreCalculator: MatchScoreCalculator

    @Autowired
    private lateinit var winnerScoreCalculator: WinnerScoreCalculator

    @Autowired
    private lateinit var predictionCleaner: PredictionCleaner

    @Autowired
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    @Autowired
    private lateinit var time: Time

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun update(): List<User> {
        predictionCleaner.deduplicate()

        var users = userRepository.findAll().map { it.toDto() }
        users.forEach { it.score = 0 }

        val predictedMatches = predictedMatchRepository.getAllMatchesWithPredictions()
                .map { it.toDto() }
                .distinctBy { Pair(it.userId, it.matchId) }

        if (!predictedMatches.none { it.hGoals != null && it.aGoals != null }) {
            users = leagueTableScoreCalculator.calculate(users, predictedMatches)
            users = matchScoreCalculator.calculate(users, predictedMatches)
            users = winnerScoreCalculator.calculate(users)
        }

        val userEntities = userRepository.saveAll(users.map { UserEntity.fromDto(it) })

        entityManager.clear()

        scoresUpdatedRepository.save(ScoresUpdatedEntity(id = 1, lastUpdated = time.localDateNow()))

        return userEntities.map { it.toDto() }
    }
}