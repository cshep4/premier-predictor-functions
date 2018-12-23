package com.cshep4.premierpredictor.matchdatarefresh.service

import com.cshep4.premierpredictor.matchdatarefresh.component.notify.NotificationDecorator
import com.cshep4.premierpredictor.matchdatarefresh.component.data.DataUpdater
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MatchDataService {
    @Autowired
    private lateinit var notificationDecorator: NotificationDecorator

    @Autowired
    private lateinit var dataUpdater: DataUpdater

    fun refresh() {
        notificationDecorator.send(dataUpdater::matchData)
    }
}