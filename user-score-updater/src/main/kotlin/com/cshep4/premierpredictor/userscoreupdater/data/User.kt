package com.cshep4.premierpredictor.userscoreupdater.data

import java.time.LocalDateTime

data class User (val id: String? = "",
                 var firstName: String = "",
                 var surname: String = "",
                 var email: String? = null,
                 var password: String? = null,
                 var predictedWinner: String = "",
                 var score: Int = 0,
                 val joined: LocalDateTime? = null,
                 var admin: Boolean = false,
                 var adFree: Boolean = false
)