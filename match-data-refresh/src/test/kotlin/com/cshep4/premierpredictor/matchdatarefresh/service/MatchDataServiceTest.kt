package com.cshep4.premierpredictor.matchdatarefresh.service

import com.cshep4.premierpredictor.matchdatarefresh.component.notify.NotificationDecorator
import com.cshep4.premierpredictor.matchdatarefresh.component.data.DataUpdater
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
internal class MatchDataServiceTest {
    @Mock
    private lateinit var notificationDecorator: NotificationDecorator

    @Mock
    private lateinit var dataUpdater: DataUpdater

    @InjectMocks
    private lateinit var matchDataService: MatchDataService

    @Test
    fun `'refresh' will send notifications and refresh match data`() {
        matchDataService.refresh()

        verify(notificationDecorator).send(dataUpdater::matchData)
        verify(dataUpdater, times(0)).matchData()
    }
}
