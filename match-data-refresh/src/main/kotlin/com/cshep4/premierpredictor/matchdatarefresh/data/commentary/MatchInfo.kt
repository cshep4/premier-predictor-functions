package com.cshep4.premierpredictor.matchdatarefresh.data.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchInfo(
		@JsonProperty("stadium")
		val stadium: String? = null,

		@JsonProperty("attendance")
		val attendance: String? = null,

		@JsonProperty("referee")
		val referee: String? = null
)
