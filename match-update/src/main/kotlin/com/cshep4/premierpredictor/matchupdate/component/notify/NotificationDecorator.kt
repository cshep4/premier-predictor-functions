package com.cshep4.premierpredictor.matchupdate.component.notify

import com.cshep4.premierpredictor.matchupdate.component.email.Emailer
import com.cshep4.premierpredictor.matchupdate.component.sms.SmsSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NotificationDecorator {
    companion object {
        const val EMAIL_ADDRESS = "shepapps4@gmail.com"
    }

    @Value("\${PHONE_NUMBER}")
    private val phoneNumber: String = "test"

    @Autowired
    private lateinit var emailer: Emailer

    @Autowired
    private lateinit var smsSender: SmsSender

    fun send(func: () -> Any) {
        val operationName = getOperationName(func.toString())

        smsSender.send(phoneNumber, "START - $operationName")
        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "START - $operationName", "START - $operationName")

        func()

        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "END - $operationName", "END - $operationName")
        smsSender.send(phoneNumber, "END - $operationName")
    }

    private fun getOperationName(func: String): String {
        return func.substring(func.indexOf(" ") + 1, func.indexOf(":"));
    }
}