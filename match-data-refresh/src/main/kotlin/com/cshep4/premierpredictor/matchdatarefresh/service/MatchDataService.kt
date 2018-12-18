package com.cshep4.premierpredictor.matchdatarefresh.service

import com.cshep4.premierpredictor.matchdatarefresh.component.email.EmailDecorator
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchDataService {
    @Autowired
    private lateinit var emailDecorator: EmailDecorator

    @Autowired
    private lateinit var matchUpdater: MatchUpdater

    fun refresh() {
        emailDecorator.operationNotification(matchUpdater::matchData)
    }
}