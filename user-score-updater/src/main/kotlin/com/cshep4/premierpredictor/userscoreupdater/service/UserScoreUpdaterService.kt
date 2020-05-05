package com.cshep4.premierpredictor.userscoreupdater.service

import com.cshep4.premierpredictor.userscoreupdater.component.notify.NotificationDecorator
import com.cshep4.premierpredictor.userscoreupdater.component.score.ScoreUpdater
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserScoreUpdaterService {
    @Autowired
    private lateinit var scoreUpdater: ScoreUpdater

    @Autowired
    private lateinit var notificationDecorator: NotificationDecorator

    fun updateScores() {
        notificationDecorator.send(scoreUpdater::update)
    }
}