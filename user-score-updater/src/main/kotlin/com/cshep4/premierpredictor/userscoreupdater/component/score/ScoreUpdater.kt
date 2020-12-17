package com.cshep4.premierpredictor.userscoreupdater.component.score

import com.cshep4.premierpredictor.userscoreupdater.component.prediction.PredictionReader
import com.cshep4.premierpredictor.userscoreupdater.component.time.Time
import com.cshep4.premierpredictor.userscoreupdater.data.LeagueUser
import com.cshep4.premierpredictor.userscoreupdater.data.User
import com.cshep4.premierpredictor.userscoreupdater.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.LeagueRepository
import com.cshep4.premierpredictor.userscoreupdater.repository.mongo.UserRepository
import com.cshep4.premierpredictor.userscoreupdater.repository.redis.ScoresUpdatedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScoreUpdater {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var predictionReader: PredictionReader

    @Autowired
    private lateinit var leagueTableScoreCalculator: LeagueTableScoreCalculator

    @Autowired
    private lateinit var matchScoreCalculator: MatchScoreCalculator

    @Autowired
    private lateinit var winnerScoreCalculator: WinnerScoreCalculator

    @Autowired
    private lateinit var scoresUpdatedRepository: ScoresUpdatedRepository

    @Autowired
    private lateinit var leagueRepository: LeagueRepository

    @Autowired
    private lateinit var time: Time

    fun update(): List<User> {
        var users = userRepository.findAll()
        users.forEach { it.score = 0 }

        val predictedMatches = predictionReader.retrieveAllPredictionsWithMatchResult()
                .distinctBy { Pair(it.userId, it.matchId) }

        if (!predictedMatches.none { it.hGoals != null && it.aGoals != null }) {
            users = leagueTableScoreCalculator.calculate(users, predictedMatches)
            users = matchScoreCalculator.calculate(users, predictedMatches)
            users = winnerScoreCalculator.calculate(users)
        }

        userRepository.save(users)

        leagueRepository.save(buildOverallTable(users))

        scoresUpdatedRepository.save(ScoresUpdatedEntity(
                id = 1,
                lastUpdated = time.localDateNow()
        ))

        return users
    }

    private fun buildOverallTable(users: List<User>): List<LeagueUser> {
        var rank = 0
        var previousScore = -1
        var usersOnScore = 1

        return users.sortedByDescending { it.score }
                .map {
                    if (it.score != previousScore) {
                        rank += usersOnScore
                        usersOnScore = 1
                    } else {
                        usersOnScore++
                    }

                    previousScore = it.score

                    LeagueUser(
                            id = it.id!!,
                            name = it.firstName + " " + it.surname,
                            predictedWinner = it.predictedWinner,
                            rank = rank,
                            score = it.score
                    )
                }
    }
}