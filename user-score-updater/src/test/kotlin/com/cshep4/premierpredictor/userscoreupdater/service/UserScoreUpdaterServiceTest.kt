package com.cshep4.premierpredictor.userscoreupdater.service

import com.cshep4.premierpredictor.userscoreupdater.component.notify.NotificationDecorator
import com.cshep4.premierpredictor.userscoreupdater.component.score.ScoreUpdater
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
internal class UserScoreUpdaterServiceTest {
    @Mock
    private lateinit var scoreUpdater: ScoreUpdater

    @Mock
    private lateinit var notificationDecorator: NotificationDecorator

    @InjectMocks
    private lateinit var userScoreUpdaterService: UserScoreUpdaterService

    @Test
    fun `'updateScores' updates user scores`() {
        userScoreUpdaterService.updateScores()

        verify(notificationDecorator).send(scoreUpdater::update)
    }
}
