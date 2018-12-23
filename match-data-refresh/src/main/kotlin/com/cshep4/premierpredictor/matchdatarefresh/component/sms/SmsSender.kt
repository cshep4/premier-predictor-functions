package com.cshep4.premierpredictor.matchdatarefresh.component.sms

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.MessageAttributeValue
import com.amazonaws.services.sns.model.PublishRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class SmsSender {
    @Autowired
    private lateinit var amazonSNS: AmazonSNS

    fun send(phoneNumber: String, message: String) {
        val senderId = MessageAttributeValue()
        senderId.dataType = "String"
        senderId.stringValue = "PremPred"

        val smsType = MessageAttributeValue()
        smsType.dataType = "String"
        smsType.stringValue = "Transactional"

        val smsAttributes = mapOf(
                Pair("AWS.SNS.SMS.SenderID", senderId),
                Pair("DefaultSMSType", smsType)
        )

        val result = amazonSNS.publish(PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes))

        System.out.println(result)
    }
}