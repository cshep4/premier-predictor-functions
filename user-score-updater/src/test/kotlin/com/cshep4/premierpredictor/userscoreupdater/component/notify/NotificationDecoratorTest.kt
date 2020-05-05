package com.cshep4.premierpredictor.userscoreupdater.component.notify

import com.cshep4.premierpredictor.userscoreupdater.component.notify.NotificationDecorator.Companion.EMAIL_ADDRESS
import com.cshep4.premierpredictor.userscoreupdater.component.email.Emailer
import com.cshep4.premierpredictor.userscoreupdater.component.messenger.Messenger
import com.cshep4.premierpredictor.userscoreupdater.component.score.ScoreUpdater
import com.nhaarman.mockito_kotlin.inOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class NotificationDecoratorTest {
    @Mock
    private lateinit var scoreUpdater: ScoreUpdater

    @Mock
    private lateinit var emailer: Emailer

    @Mock
    private lateinit var messenger: Messenger

    @InjectMocks
    private lateinit var notificationDecorator: NotificationDecorator

    @Test
    fun `'send' will send an email and sms before and after the operation is carried out`() {
        val operationName = "com.cshep4.premierpredictor.userscoreupdater.component.score.ScoreUpdater.update()"

        val start = "START - $operationName"
        val end = "END - $operationName"

        notificationDecorator.send(scoreUpdater::update)

        val inOrder = inOrder(
                messenger,
                emailer,
                scoreUpdater,
                emailer,
                messenger
        )

        inOrder.verify(messenger).send(start)
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, start, start)
        inOrder.verify(scoreUpdater).update()
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, end, end)
        inOrder.verify(messenger).send(end)
    }
}