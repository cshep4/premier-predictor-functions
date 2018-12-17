package com.cshep4.premierpredictor.matchupdate.component.email

import com.cshep4.premierpredictor.matchupdate.component.email.EmailDecorator.Companion.EMAIL_ADDRESS
import com.cshep4.premierpredictor.matchupdate.component.score.UserScoreUpdater
import com.nhaarman.mockito_kotlin.inOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class EmailDecoratorTest {
    @Mock
    private lateinit var userScoreUpdater: UserScoreUpdater

    @Mock
    private lateinit var emailer: Emailer

    @InjectMocks
    private lateinit var emailDecorator: EmailDecorator

    @Test
    fun `'operationNotification' will send an email before and after the operation is carried out`() {
        val operationName = "com.cshep4.premierpredictor.matchupdate.component.score.UserScoreUpdater.update()"

        val start = "START - $operationName"
        val end = "END - $operationName"

        emailDecorator.operationNotification(userScoreUpdater::update)

        val inOrder = inOrder(emailer, userScoreUpdater, emailer)

        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, start, start)
        inOrder.verify(userScoreUpdater).update()
        inOrder.verify(emailer).send(EMAIL_ADDRESS, EMAIL_ADDRESS, end, end)
    }
}