package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.DataRetriever
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class MatchFactsRetriever : Retriever<MatchFacts> {
    @Autowired
    @Qualifier("liveScore")
    private lateinit var dataRetriever: DataRetriever

    @Autowired
    private lateinit var time: Time

    override fun getLatest(id: String): MatchFacts? {
        val apiResult = dataRetriever.retrieveMatch(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult.toSantisedMatchFacts()
    }
}
