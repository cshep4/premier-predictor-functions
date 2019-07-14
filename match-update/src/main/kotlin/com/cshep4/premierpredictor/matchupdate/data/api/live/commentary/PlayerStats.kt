package com.cshep4.premierpredictor.matchupdate.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class PlayerStats(
        @JsonProperty("localteam")
        var localTeam: Players? = null,

        @JsonProperty("visitorteam")
        var visitorTeam: Players? = null
)

data class Players(
        @JsonProperty("player")
        var player: List<Player>? = null
)

data class Player(
        @JsonProperty("id")
        var id: String? = null,

        @JsonProperty("num")
        var num: String? = null,

        @JsonProperty("name")
        var name: String? = null,

        @JsonProperty("pos")
        var pos: String? = null,

        @JsonProperty("posx")
        var posX: String? = null,

        @JsonProperty("posy")
        var posY: String? = null,

        @JsonProperty("shots_total")
        var shotsTotal: String? = null,

        @JsonProperty("shots_on_goal")
        var shotsOnGoal: String? = null,

        @JsonProperty("goals")
        var goals: String? = null,

        @JsonProperty("assists")
        var assists: String? = null,

        @JsonProperty("offsides")
        var offsides: String? = null,

        @JsonProperty("fouls_drawn")
        var foulsDrawn: String? = null,

        @JsonProperty("fouls_committed")
        var foulsCommitted: String? = null,

        @JsonProperty("saves")
        var saves: String? = null,

        @JsonProperty("yellowcards")
        var yellowCards: String? = null,

        @JsonProperty("redcards")
        var redCards: String? = null,

        @JsonProperty("pen_score")
        var penScore: String? = null,

        @JsonProperty("pen_miss")
        var penMiss: String? = null
)