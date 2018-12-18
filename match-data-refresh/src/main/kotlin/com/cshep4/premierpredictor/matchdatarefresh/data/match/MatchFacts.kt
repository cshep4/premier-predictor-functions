package com.cshep4.premierpredictor.matchdatarefresh.data.match

import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.correctStatus
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.getFullTeamName
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.sanitiseScore
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

data class MatchFacts(
		@JsonProperty("id")
		val id: String? = null,

		@JsonProperty("comp_id")
		val compId: String? = null,

		@JsonProperty("formatted_date")
		var formattedDate: String? = null,

		@JsonProperty("season")
		val season: String? = null,

		@JsonProperty("week")
		val week: String? = null,

		@JsonProperty("venue")
		val venue: String? = null,

		@JsonProperty("venue_id")
		val venueId: String? = null,

		@JsonProperty("venue_city")
		val venueCity: String? = null,

		@JsonProperty("status")
		var status: String? = null,

		@JsonProperty("timer")
		var timer: String? = null,

		@JsonProperty("time")
		var time: String? = null,

		@JsonProperty("localteam_id")
		val localTeamId: String? = null,

		@JsonProperty("localteam_name")
		var localTeamName: String? = null,

		@JsonProperty("localteam_score")
		var localTeamScore: String? = null,

		@JsonProperty("visitorteam_id")
		val visitorTeamId: String? = null,

		@JsonProperty("visitorteam_name")
		var visitorTeamName: String? = null,

		@JsonProperty("visitorteam_score")
		var visitorTeamScore: String? = null,

		@JsonProperty("ht_score")
		val htScore: String? = null,

		@JsonProperty("ft_score")
		val ftScore: String? = null,

		@JsonProperty("et_score")
		val etScore: String? = null,

		@JsonProperty("penalty_local")
		val penaltyLocal: String? = null,

		@JsonProperty("penalty_visitor")
		val penaltyVisitor: String? = null,

		@JsonProperty("events")
		val events: List<Event>? = null,

		@JsonIgnore
		@JsonProperty("commentary")
		var commentary: Commentary? = null,

		@JsonIgnore
		@JsonProperty("lastUpdated")
		@JsonSerialize(using = LocalDateTimeSerializer::class)
		@JsonDeserialize(using = LocalDateTimeDeserializer::class)
		var lastUpdated: LocalDateTime? = LocalDateTime.now(Clock.systemUTC())
) {
	fun toSantisedMatchFacts() = MatchFacts(
			penaltyVisitor = this.penaltyVisitor,
			venue = this.venue,
			week = this.week,
			visitorTeamName = getFullTeamName(this.visitorTeamName),
			penaltyLocal = this.penaltyLocal,
			localTeamScore = sanitiseScore(this.localTeamScore),
			ftScore = this.ftScore,
			etScore = this.etScore,
			compId = this.compId,
			venueCity = this.venueCity,
			visitorTeamId = this.visitorTeamId,
			timer = this.timer,
			htScore = this.htScore,
			localTeamId = this.localTeamId,
			season = this.season,
			localTeamName = getFullTeamName(this.localTeamName),
			id = this.id,
			time = this.time,
			visitorTeamScore = sanitiseScore(this.visitorTeamScore),
			formattedDate = this.formattedDate,
			venueId = this.venueId,
			events = this.events,
			status = correctStatus(this.status),
			commentary = this.commentary,
			lastUpdated = this.lastUpdated)

	@JsonIgnore
	fun getDateTime(): LocalDateTime? {
		val time = LocalTime.parse(this.time)
		val date = LocalDate.parse(this.formattedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))

		return LocalDateTime.of(date, time)
	}

	@JsonIgnore
	fun setDateTime(localDateTime: LocalDateTime) {
		val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
		this.time = localDateTime.format(timeFormatter)

		val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
		this.formattedDate = localDateTime.format(dateFormatter)
	}

	private fun getPlayed(): Int {
		return if (this.localTeamScore != null && this.localTeamScore != "" && this.visitorTeamScore != null && this.visitorTeamScore != "") {
			1
		} else {
			0
		}
	}
}
