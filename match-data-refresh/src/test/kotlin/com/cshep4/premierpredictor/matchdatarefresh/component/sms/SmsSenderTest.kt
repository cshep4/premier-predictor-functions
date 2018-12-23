package com.cshep4.premierpredictor.matchdatarefresh.component.sms

import com.amazonaws.services.sns.AmazonSNS
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class SmsSenderTest {
    @Mock
    private lateinit var amazonSns: AmazonSNS

    @InjectMocks
    private lateinit var smsSender: SmsSender

    @Test
    fun `'send' sends sms with phone number and message`() {
        smsSender.send("mobile number", "test message")

        verify(amazonSns).publish(any())
    }
}