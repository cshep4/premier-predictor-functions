package com.cshep4.premierpredictor.matchdatarefresh.functions

import com.cshep4.premierpredictor.matchdatarefresh.domain.Request
import com.cshep4.premierpredictor.matchdatarefresh.domain.Response
import com.cshep4.premierpredictor.matchdatarefresh.service.MatchDataService
import org.springframework.beans.factory.annotation.Autowired
import java.util.function.Function
import org.springframework.http.HttpStatus.OK
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

class MatchDataRefresh : Function<Request, Response> {
    @Autowired
    private lateinit var matchDataService: MatchDataService

    override fun apply(request: Request): Response {
        matchDataService.refresh()

        return Response(statusCode = OK.value())
    }
}