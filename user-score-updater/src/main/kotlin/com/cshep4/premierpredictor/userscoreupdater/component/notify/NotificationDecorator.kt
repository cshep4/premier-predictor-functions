package com.cshep4.premierpredictor.userscoreupdater.component.notify

import com.cshep4.premierpredictor.userscoreupdater.component.email.Emailer
import com.cshep4.premierpredictor.userscoreupdater.component.messenger.Messenger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class NotificationDecorator {
    companion object {
        const val EMAIL_ADDRESS = "shepapps4@gmail.com"
    }

    @Autowired
    private lateinit var emailer: Emailer

    @Autowired
    private lateinit var messenger: Messenger

    fun send(func: () -> Any) {
        val operationName = getOperationName(func.toString())

        messenger.send("START - $operationName")
        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "START - $operationName", "START - $operationName")

        func()

        emailer.send(EMAIL_ADDRESS, EMAIL_ADDRESS, "END - $operationName", "END - $operationName")
        messenger.send("END - $operationName")
    }

    private fun getOperationName(func: String): String {
        return func.substring(func.indexOf(" ") + 1, func.indexOf(":"))
    }
}