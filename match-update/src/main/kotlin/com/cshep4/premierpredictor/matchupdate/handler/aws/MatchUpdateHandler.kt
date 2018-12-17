package com.cshep4.premierpredictor.matchupdate.handler.aws

import com.cshep4.premierpredictor.matchupdate.domain.Request
import com.cshep4.premierpredictor.matchupdate.domain.Response
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler

class MatchUpdateHandler() : SpringBootRequestHandler<Request, Response>()