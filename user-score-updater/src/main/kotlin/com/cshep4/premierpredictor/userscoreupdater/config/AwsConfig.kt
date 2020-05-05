package com.cshep4.premierpredictor.userscoreupdater.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions.US_EAST_1
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AwsConfig {

    @Value("\${amazon.aws.accesskey}")
    private val amazonAWSAccessKey: String? = null

    @Value("\${amazon.aws.secretkey}")
    private val amazonAWSSecretKey: String? = null

    fun amazonAWSCredentialsProvider(): AWSCredentialsProvider {
        return AWSStaticCredentialsProvider(amazonAWSCredentials())
    }

    @Bean
    fun amazonAWSCredentials(): AWSCredentials {
        return BasicAWSCredentials(amazonAWSAccessKey!!, amazonAWSSecretKey!!)
    }

    @Bean
    fun amazonSns(): AmazonSNS {
        return AmazonSNSClientBuilder.standard()
                .withCredentials(amazonAWSCredentialsProvider())
                .withRegion(US_EAST_1)
                .build()
    }
}