package com.cshep4.premierpredictor.matchupdate.component.email

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.sendgrid.Response
import com.sendgrid.SendGrid
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class EmailerTest {
    companion object {
        const val EMAIL_ADDRESS = "shepapps4@gmail.com"
        const val SUBJECT = "Subject"
        const val CONTENT = "Content"
    }

    @Mock
    private lateinit var sendGrid: SendGrid

    @InjectMocks
    private lateinit var emailer: Emailer

    @Test
    fun `'send' makes a request to sendgrid with correct details`() {
        whenever(sendGrid.api(any())).thenReturn(Response())

        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, SUBJECT, CONTENT)

        verify(sendGrid).api(any())
    }
}