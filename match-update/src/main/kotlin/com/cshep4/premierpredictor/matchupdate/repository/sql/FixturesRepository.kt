package com.cshep4.premierpredictor.matchupdate.repository.sql

import com.cshep4.premierpredictor.matchupdate.entity.MatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixturesRepository : JpaRepository<MatchEntity, Long>