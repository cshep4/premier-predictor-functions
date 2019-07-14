package com.cshep4.premierpredictor.matchupdate.data.api.live.commentary

import com.fasterxml.jackson.annotation.JsonProperty

data class Substitutions(
        @JsonProperty("localteam")
        var localTeam: List<Substitution>? = null,

        @JsonProperty("visitorteam")
        var visitorTeam: List<Substitution>? = null
)

data class Substitution(
        @JsonProperty("off_name")
        var offName: String? = null,

        @JsonProperty("on_name")
        var onName: String? = null,

        @JsonProperty("off_id")
        var offId: String? = null,

        @JsonProperty("on_id")
        var onId: String? = null,

        @JsonProperty("minute")
        var minute: String? = null,

        @JsonProperty("table_id")
        var tableId: String? = null
)