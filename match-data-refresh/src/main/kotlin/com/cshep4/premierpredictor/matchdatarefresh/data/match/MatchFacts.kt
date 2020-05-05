package com.cshep4.premierpredictor.matchdatarefresh.data.match

import com.cshep4.premierpredictor.matchdatarefresh.data.Match
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
import java.time.format.DateTimeParseException
import java.util.*

data class MatchFacts(
		@JsonProperty("id")
		var id: String? = null,

		@JsonProperty("comp_id")
		var compId: String? = null,

		@JsonProperty("formatted_date")
		var formattedDate: String? = null,

		@JsonProperty("season")
		var season: String? = null,

		@JsonProperty("week")
		var week: String? = null,

		@JsonProperty("venue")
		var venue: String? = null,

		@JsonProperty("venue_id")
		var venueId: String? = null,

		@JsonProperty("venue_city")
		var venueCity: String? = null,

		@JsonProperty("status")
		var status: String? = null,

		@JsonProperty("timer")
		var timer: String? = null,

		@JsonProperty("time")
		var time: String? = null,

		@JsonProperty("localteam_id")
		var localTeamId: String? = null,

		@JsonProperty("localteam_name")
		var localTeamName: String? = null,

		@JsonProperty("localteam_score")
		var localTeamScore: String? = null,

		@JsonProperty("visitorteam_id")
		var visitorTeamId: String? = null,

		@JsonProperty("visitorteam_name")
		var visitorTeamName: String? = null,

		@JsonProperty("visitorteam_score")
		var visitorTeamScore: String? = null,

		@JsonProperty("ht_score")
		var htScore: String? = null,

		@JsonProperty("ft_score")
		var ftScore: String? = null,

		@JsonProperty("et_score")
		var etScore: String? = null,

		@JsonProperty("penalty_local")
		var penaltyLocal: String? = null,

		@JsonProperty("penalty_visitor")
		var penaltyVisitor: String? = null,

		@JsonProperty("events")
		var events: List<Event>? = null,

		@JsonIgnore
		@JsonProperty("commentary")
		var commentary: Commentary? = null,

		@JsonIgnore
		@JsonProperty("lastUpdated")
		@JsonSerialize(using = LocalDateTimeSerializer::class)
		@JsonDeserialize(using = LocalDateTimeDeserializer::class)
		var lastUpdated: LocalDateTime? = LocalDateTime.now(Clock.systemUTC())
) {

	fun toMatch(): Match = Match(
			id = this.id!!,
			hTeam = getFullTeamName(this.localTeamName)!!,
			aTeam = getFullTeamName(this.visitorTeamName)!!,
			hGoals = this.localTeamScore?.toIntOrNull(),
			aGoals = this.visitorTeamScore?.toIntOrNull(),
			played = getPlayed(),
			dateTime = getDateTime(),
			matchday = this.week!!.toInt())

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
	fun wasRescheduled(): Boolean {
		return try {
			getDateTime()

			status in arrayOf("Postp.", "Cancl.", "Awarded", "Aban.")
		} catch (e: DateTimeParseException) {
			true
		}
	}

	@JsonIgnore
	fun getDateTime(): LocalDateTime? {
		var time = LocalTime.parse(this.time)
		var date = LocalDate.parse(this.formattedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))

		return LocalDateTime.of(date, time)
	}

	@JsonIgnore
	fun setDateTime(localDateTime: LocalDateTime) {
		var timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
		this.time = localDateTime.format(timeFormatter)

		var dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH)
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
