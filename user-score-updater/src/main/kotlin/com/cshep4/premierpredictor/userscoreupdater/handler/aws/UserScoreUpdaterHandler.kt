package com.cshep4.premierpredictor.userscoreupdater.handler.aws

import com.cshep4.premierpredictor.userscoreupdater.domain.Request
import com.cshep4.premierpredictor.userscoreupdater.domain.Response
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler

class UserScoreUpdaterHandler : SpringBootRequestHandler<Request, Response>()