package com.cshep4.premierpredictor.matchdatarefresh.data.match

import com.fasterxml.jackson.annotation.JsonProperty

data class Event(
		@JsonProperty("id")
		var id: String? = null,

		@JsonProperty("type")
		var type: String? = null,

		@JsonProperty("result")
		var result: String? = null,

		@JsonProperty("minute")
		var minute: String? = null,

		@JsonProperty("extra_min")
		var extraMin: String? = null,

		@JsonProperty("team")
		var team: String? = null,

		@JsonProperty("player")
		var player: String? = null,

		@JsonProperty("player_id")
		var playerId: String? = null,

		@JsonProperty("assist")
		var assist: String? = null,

		@JsonProperty("assist_id")
		var assistId: String? = null
)
