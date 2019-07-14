package com.cshep4.premierpredictor.matchdatarefresh.data.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class MatchStats(
        @JsonProperty("localteam")
        var localTeam: List<TeamStats>? = null,

        @JsonProperty("visitorteam")
        var visitorTeam: List<TeamStats>? = null
)

data class TeamStats(
        @JsonProperty("shots_total")
        var shotsTotal: String? = null,

        @JsonProperty("shots_ongoal")
        var shotsOnGoal: String? = null,

        @JsonProperty("fouls")
        var fouls: String? = null,

        @JsonProperty("corners")
        var corners: String? = null,

        @JsonProperty("offsides")
        var offsides: String? = null,

        @JsonProperty("possesiontime")
        var possessionTime: String? = null,

        @JsonProperty("yellowcards")
        var yellowCards: String? = null,

        @JsonProperty("redcards")
        var redCards: String? = null,

        @JsonProperty("saves")
        var saves: String? = null,

        @JsonProperty("table_id")
        var tableId: String? = null
)