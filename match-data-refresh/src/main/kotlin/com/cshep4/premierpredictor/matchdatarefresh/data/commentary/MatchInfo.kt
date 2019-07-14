package com.cshep4.premierpredictor.matchdatarefresh.data.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchInfo(
		@JsonProperty("stadium")
		var stadium: String? = null,

		@JsonProperty("attendance")
		var attendance: String? = null,

		@JsonProperty("referee")
		var referee: String? = null
)
