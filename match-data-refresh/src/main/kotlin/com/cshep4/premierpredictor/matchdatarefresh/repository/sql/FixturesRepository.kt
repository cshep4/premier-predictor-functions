package com.cshep4.premierpredictor.matchdatarefresh.repository.sql

import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixturesRepository : JpaRepository<MatchEntity, Long>