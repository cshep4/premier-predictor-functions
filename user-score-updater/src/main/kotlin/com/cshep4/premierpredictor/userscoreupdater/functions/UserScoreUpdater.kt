package com.cshep4.premierpredictor.userscoreupdater.functions

import com.cshep4.premierpredictor.userscoreupdater.domain.Request
import com.cshep4.premierpredictor.userscoreupdater.domain.Response
import com.cshep4.premierpredictor.userscoreupdater.service.UserScoreUpdaterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import java.util.function.Function

class UserScoreUpdater : Function<Request, Response> {
    @Autowired
    private lateinit var userScoreUpdaterService: UserScoreUpdaterService

    override fun apply(request: Request): Response {
        userScoreUpdaterService.updateScores()
        return Response(statusCode = OK.value())
    }
}