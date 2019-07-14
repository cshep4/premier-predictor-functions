package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.ApiRequester
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
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
