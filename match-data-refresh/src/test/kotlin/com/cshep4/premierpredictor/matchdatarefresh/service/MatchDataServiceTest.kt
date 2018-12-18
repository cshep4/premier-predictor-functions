package com.cshep4.premierpredictor.matchdatarefresh.service

import com.cshep4.premierpredictor.matchdatarefresh.component.email.EmailDecorator
import com.cshep4.premierpredictor.matchdatarefresh.component.match.MatchUpdater
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
    private lateinit var emailDecorator: EmailDecorator

    @Mock
    private lateinit var matchUpdater: MatchUpdater

    @InjectMocks
    private lateinit var matchDataService: MatchDataService

    @Test
    fun `'refresh' will send email notifications and refresh match data`() {
        matchDataService.refresh()

        verify(emailDecorator).operationNotification(matchUpdater::matchData)
        verify(matchUpdater, times(0)).matchData()
    }
}
