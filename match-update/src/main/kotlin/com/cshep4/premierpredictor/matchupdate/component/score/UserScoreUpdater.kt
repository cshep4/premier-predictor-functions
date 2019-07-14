package com.cshep4.premierpredictor.matchupdate.component.score

import com.cshep4.premierpredictor.matchupdate.component.prediction.PredictionReader
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.User
import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import com.cshep4.premierpredictor.matchupdate.repository.mongo.UserRepository
import com.cshep4.premierpredictor.matchupdate.repository.redis.ScoresUpdatedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserScoreUpdater {
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

        scoresUpdatedRepository.save(ScoresUpdatedEntity(id = 1, lastUpdated = time.localDateNow()))

        return users
    }
}