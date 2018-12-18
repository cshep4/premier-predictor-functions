package com.cshep4.premierpredictor.matchdatarefresh.component.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EmailDecorator {
    companion object {
        const val EMAIL_ADDRESS = "shepapps4@gmail.com"
    }
    @Autowired
    private lateinit var emailer: Emailer

    fun operationNotification(func: () -> Any) {
        val operationName = getOperationName(func.toString())

        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "START - $operationName", "START - $operationName")

        func()

        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "END - $operationName", "END - $operationName")
    }

    private fun getOperationName(func: String): String {
        return func.substring(func.indexOf(" ") + 1, func.indexOf(":"));
    }
}