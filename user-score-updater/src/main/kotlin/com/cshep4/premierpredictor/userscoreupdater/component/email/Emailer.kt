package com.cshep4.premierpredictor.userscoreupdater.component.email

import com.sendgrid.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class Emailer {
    @Autowired
    private lateinit var sendGrid: SendGrid

    fun send(sender: String, recipient: String, subject: String, emailContent: String) {
        val from = Email(sender)
        val to = Email(recipient)
        val content = Content("text/plain", emailContent)
        val mail = Mail(from, subject, to, content)

        val request = Request()
        try {
            request.method = Method.POST
            request.endpoint = "mail/send"
            request.body = mail.build()
            val response = sendGrid.api(request)
            System.out.println(response.statusCode)
            System.out.println(response.body)
            System.out.println(response.headers)
        } catch (ex: IOException) {
            throw ex
        }
    }
}