package com.cshep4.premierpredictor.matchupdate.functions

import com.cshep4.premierpredictor.matchupdate.domain.Request
import com.cshep4.premierpredictor.matchupdate.domain.Response
import com.cshep4.premierpredictor.matchupdate.service.UpdateMatchService
import org.springframework.beans.factory.annotation.Autowired
import java.util.function.Function
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

class MatchUpdate : Function<Request, Response> {
    @Autowired
    private lateinit var updateMatchService: UpdateMatchService

    override fun apply(request: Request): Response {
        return when (updateMatchService.updateLiveMatches()) {
            true -> Response(statusCode = OK.value())
            false -> Response(statusCode = INTERNAL_SERVER_ERROR.value())
        }
    }
}