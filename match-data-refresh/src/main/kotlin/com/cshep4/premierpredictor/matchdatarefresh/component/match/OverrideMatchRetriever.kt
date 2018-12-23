package com.cshep4.premierpredictor.matchdatarefresh.component.match

import com.cshep4.premierpredictor.matchdatarefresh.data.OverrideMatch
import com.cshep4.premierpredictor.matchdatarefresh.repository.sql.OverrideMatchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OverrideMatchRetriever {
    @Autowired
    private lateinit var overrideMatchRepository: OverrideMatchRepository

    fun findAll() : List<OverrideMatch> = overrideMatchRepository.findAll()
            .filter { it.hGoals != null && it.aGoals != null }
            .map { it.toDto() }
}