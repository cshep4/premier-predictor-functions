package com.cshep4.premierpredictor.matchdatarefresh.data.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Lineup(
        @JsonProperty("localteam")
        var localTeam: List<Position>? = null,

        @JsonProperty("visitorteam")
        var visitorTeam: List<Position>? = null
)

data class Position(
        @JsonProperty("id")
        var id: String? = null,

        @JsonProperty("number")
        var number: String? = null,

        @JsonProperty("name")
        var name: String? = null,

        @JsonProperty("pos")
        var pos: String? = null
)