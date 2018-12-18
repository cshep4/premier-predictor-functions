package com.cshep4.premierpredictor.matchdatarefresh.handler.aws

import com.cshep4.premierpredictor.matchdatarefresh.domain.Request
import com.cshep4.premierpredictor.matchdatarefresh.domain.Response
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler

class MatchDataRefreshHandler() : SpringBootRequestHandler<Request, Response>()