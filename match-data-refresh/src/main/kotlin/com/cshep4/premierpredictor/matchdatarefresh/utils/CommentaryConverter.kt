package com.cshep4.premierpredictor.matchdatarefresh.utils

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.fasterxml.jackson.databind.ObjectMapper


class CommentaryConverter : DynamoDBTypeConverter<String, Commentary> {

    override fun convert(commentary: Commentary): String {
        return ObjectMapper().writeValueAsString(commentary)
    }

    override fun unconvert(stringValue: String): Commentary? {
        return ObjectMapper().readValue(stringValue, Commentary::class.java)
    }
}