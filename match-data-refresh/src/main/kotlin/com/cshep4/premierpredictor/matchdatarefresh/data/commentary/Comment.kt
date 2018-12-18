package com.cshep4.premierpredictor.matchdatarefresh.data.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Comment(
		@JsonProperty("id")
		val id: String? = null,

		@JsonProperty("important")
		val important: String? = null,

		@JsonProperty("isgoal")
		val goal: String? = null,

		@JsonProperty("minute")
		val minute: String? = null,

		@JsonProperty("comment")
		val comment: String? = null
)
