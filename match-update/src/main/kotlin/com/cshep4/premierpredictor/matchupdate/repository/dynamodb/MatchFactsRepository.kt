package com.cshep4.premierpredictor.matchupdate.repository.dynamodb

import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface MatchFactsRepository : CrudRepository<MatchFactsEntity, String>