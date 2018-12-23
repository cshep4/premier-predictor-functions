package com.cshep4.premierpredictor.matchdatarefresh.repository.sql

import com.cshep4.premierpredictor.matchdatarefresh.entity.OverrideMatchEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OverrideMatchRepository : JpaRepository<OverrideMatchEntity, Long>