package com.cshep4.premierpredictor.matchdatarefresh.repository

import com.cshep4.premierpredictor.matchdatarefresh.entity.MatchFactsEntity
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface MatchFactsRepository : CrudRepository<MatchFactsEntity, String>