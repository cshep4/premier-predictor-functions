package com.cshep4.premierpredictor.matchupdate.repository.redis

import com.cshep4.premierpredictor.matchupdate.entity.LiveMatchEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LiveMatchRepository : CrudRepository<LiveMatchEntity, String>