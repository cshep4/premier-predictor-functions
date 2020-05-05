package com.cshep4.premierpredictor.userscoreupdater.repository.redis

import com.cshep4.premierpredictor.userscoreupdater.entity.ScoresUpdatedEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoresUpdatedRepository : CrudRepository<ScoresUpdatedEntity, Int>