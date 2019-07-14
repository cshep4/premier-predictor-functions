package com.cshep4.premierpredictor.matchupdate.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchInfo(
		@JsonProperty("stadium")
		var stadium: String? = null,

		@JsonProperty("attendance")
		var attendance: String? = null,

		@JsonProperty("referee")
		var referee: String? = null
)
