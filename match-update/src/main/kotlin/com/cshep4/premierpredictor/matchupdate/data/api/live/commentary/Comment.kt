package com.cshep4.premierpredictor.matchupdate.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Comment(
		@JsonProperty("id")
		var id: String? = null,

		@JsonProperty("important")
		var important: String? = null,

		@JsonProperty("isgoal")
		var goal: String? = null,

		@JsonProperty("minute")
		var minute: String? = null,

		@JsonProperty("comment")
		var comment: String? = null
)
