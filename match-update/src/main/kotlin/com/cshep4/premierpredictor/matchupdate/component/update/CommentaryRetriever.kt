package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.DataRetriever
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class CommentaryRetriever: Retriever<Commentary> {
    @Autowired
    @Qualifier("liveScore")
    private lateinit var dataRetriever: DataRetriever

    @Autowired
    private lateinit var time: Time

    override fun getLatest(id: String): Commentary? {
        val apiResult = dataRetriever.retrieveCommentary(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult
    }
}