package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.ApiRequester
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.cshep4.premierpredictor.matchupdate.entity.MatchFactsEntity
import com.cshep4.premierpredictor.matchupdate.repository.dynamodb.MatchFactsRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MatchFactsRetriever: Retriever<MatchFacts> {
    @Autowired
    private lateinit var apiRequester: ApiRequester

    @Autowired
    private lateinit var time: Time

    override fun getLatest(id: String): MatchFacts? {
        val apiResult = apiRequester.retrieveMatch(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult.toSantisedMatchFacts()
    }
}
