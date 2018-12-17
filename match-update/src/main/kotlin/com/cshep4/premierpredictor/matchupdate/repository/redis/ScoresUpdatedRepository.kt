package com.cshep4.premierpredictor.matchupdate.repository.redis

import com.cshep4.premierpredictor.matchupdate.entity.ScoresUpdatedEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScoresUpdatedRepository : CrudRepository<ScoresUpdatedEntity, Int>