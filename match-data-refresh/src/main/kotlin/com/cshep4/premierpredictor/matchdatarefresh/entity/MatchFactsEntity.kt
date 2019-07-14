package com.cshep4.premierpredictor.matchdatarefresh.entity

import com.cshep4.premierpredictor.matchdatarefresh.data.commentary.Commentary
import com.cshep4.premierpredictor.matchdatarefresh.data.match.Event
import com.cshep4.premierpredictor.matchdatarefresh.data.match.MatchFacts
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.correctStatus
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.getFullTeamName
import com.cshep4.premierpredictor.matchdatarefresh.utils.MatchFactUtils.sanitiseScore
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.springframework.data.annotation.Id
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

data class MatchFactsEntity(
        @Id
        var id: String? = null,

        var penaltyVisitor: String? = null,

        var venue: String? = null,

        var week: String? = null,

        var visitorTeamName: String? = null,

        var penaltyLocal: String? = null,

        var localTeamScore: String? = null,

        var ftScore: String? = null,

        var etScore: String? = null,

        var compId: String? = null,

        var venueCity: String? = null,

        var visitorTeamId: String? = null,

        var timer: String? = null,

        var htScore: String? = null,

        var localTeamId: String? = null,

        var season: String? = null,

        var localTeamName: String? = null,

        var time: String? = null,

        var visitorTeamScore: String? = null,

        var formattedDate: String? = null,

        var venueId: String? = null,

        var events: List<Event>? = null,

        var status: String? = null,

        var commentary: Commentary? = null,

        var lastUpdated: LocalDateTime? = LocalDateTime.now(Clock.systemUTC()),

        var matchDate: LocalDate? = null
){
    fun toDto(): MatchFacts = MatchFacts(
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

    companion object {
        fun fromDto(dto: MatchFacts) = MatchFactsEntity(
                penaltyVisitor = dto.penaltyVisitor,
                venue = dto.venue,
                week = dto.week,
                visitorTeamName = getFullTeamName(dto.visitorTeamName),
                penaltyLocal = dto.penaltyLocal,
                localTeamScore = sanitiseScore(dto.localTeamScore),
                ftScore = dto.ftScore,
                etScore = dto.etScore,
                compId = dto.compId,
                venueCity = dto.venueCity,
                visitorTeamId = dto.visitorTeamId,
                timer = dto.timer,
                htScore = dto.htScore,
                localTeamId = dto.localTeamId,
                season = dto.season,
                localTeamName = getFullTeamName(dto.localTeamName),
                id = dto.id,
                time = dto.time,
                visitorTeamScore = sanitiseScore(dto.visitorTeamScore),
                formattedDate = dto.formattedDate,
                venueId = dto.venueId,
                events = dto.events,
                status = correctStatus(dto.status),
                commentary = dto.commentary,
                lastUpdated = dto.lastUpdated,
                matchDate = toMatchDate(dto.formattedDate))

        private fun toMatchDate(formattedDate: String?): LocalDate? {
            formattedDate ?: return null

            return try {
                LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH))
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }
}