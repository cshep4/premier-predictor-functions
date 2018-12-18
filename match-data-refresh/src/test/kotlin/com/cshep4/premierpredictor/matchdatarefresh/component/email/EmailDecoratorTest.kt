package com.cshep4.premierpredictor.matchdatarefresh.component.email

import com.cshep4.premierpredictor.matchdatarefresh.component.email.EmailDecorator.Companion.EMAIL_ADDRESS
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
import com.nhaarman.mockito_kotlin.inOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class EmailDecoratorTest {
    @Mock
    private lateinit var matchUpdater: MatchUpdater

    @Mock
    private lateinit var emailer: Emailer

    @InjectMocks
    private lateinit var emailDecorator: EmailDecorator

    @Test
    fun `'operationNotification' will send an email before and after the operation is carried out`() {
        val operationName = "com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater.matchData()"

        val start = "START - $operationName"
        val end = "END - $operationName"

        emailDecorator.operationNotification(matchUpdater::matchData)

        val inOrder = inOrder(emailer, matchUpdater, emailer)

        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, start, start)
        inOrder.verify(matchUpdater).matchData()
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, end, end)
    }
}