package com.cshep4.premierpredictor.matchupdate.functions

import com.cshep4.premierpredictor.matchupdate.domain.Request
import com.cshep4.premierpredictor.matchupdate.domain.Response
import com.cshep4.premierpredictor.matchupdate.service.UpdateMatchService
import org.springframework.beans.factory.annotation.Autowired
import java.util.function.Function

class MatchUpdate : Function<Request, Response> {
    @Autowired
    private lateinit var updateMatchService: UpdateMatchService

    override fun apply(request: Request): Response {
        return Response(
                next = updateMatchService.updateLiveMatches()
        )
    }
}