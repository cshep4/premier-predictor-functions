package com.cshep4.premierpredictor.matchupdate.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.Clock
import java.time.LocalDateTime

data class Commentary(
		@JsonProperty("match_id")
		var matchId: String? = null,

		@JsonProperty("match_info")
		var matchInfo: List<MatchInfo>? = null,

		@JsonProperty("lineup")
		var lineup: Lineup? = null,

		@JsonProperty("subs")
		var subs: Lineup? = null,

		@JsonProperty("substitutions")
		var substitutions: Substitutions? = null,

		@JsonProperty("comments")
		var comments: List<Comment>? = null,

		@JsonProperty("match_stats")
		var matchStats: MatchStats? = null,

		@JsonProperty("player_stats")
		var playerStats: PlayerStats? = null,

		@JsonIgnore
		@JsonProperty("lastUpdated")
		@JsonSerialize(using = LocalDateTimeSerializer::class)
		@JsonDeserialize(using = LocalDateTimeDeserializer::class)
		var lastUpdated: LocalDateTime? = LocalDateTime.now(Clock.systemUTC())
)
