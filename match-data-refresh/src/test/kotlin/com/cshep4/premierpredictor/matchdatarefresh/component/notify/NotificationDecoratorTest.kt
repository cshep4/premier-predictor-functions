package com.cshep4.premierpredictor.matchdatarefresh.component.notify

import com.cshep4.premierpredictor.matchdatarefresh.component.notify.NotificationDecorator.Companion.EMAIL_ADDRESS
import com.cshep4.premierpredictor.matchdatarefresh.component.data.DataUpdater
import com.cshep4.premierpredictor.matchdatarefresh.component.email.Emailer
import com.cshep4.premierpredictor.matchdatarefresh.component.sms.SmsSender
import com.nhaarman.mockito_kotlin.inOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class NotificationDecoratorTest {
    @Mock
    private lateinit var dataUpdater: DataUpdater

    @Mock
    private lateinit var emailer: Emailer

    @Mock
    private lateinit var smsSender: SmsSender

    @InjectMocks
    private lateinit var notificationDecorator: NotificationDecorator

    @Test
    fun `'send' will send an email and sms before and after the operation is carried out`() {
        val operationName = "com.cshep4.premierpredictor.matchdatarefresh.component.data.DataUpdater.matchData()"

        val start = "START - $operationName"
        val end = "END - $operationName"

        notificationDecorator.send(dataUpdater::matchData)

        val inOrder = inOrder(
                smsSender,
                emailer,
                dataUpdater,
                emailer,
                smsSender
        )

        inOrder.verify(smsSender).send("test", start)
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, start, start)
        inOrder.verify(dataUpdater).matchData()
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, end, end)
        inOrder.verify(smsSender).send("test", end)
    }
}