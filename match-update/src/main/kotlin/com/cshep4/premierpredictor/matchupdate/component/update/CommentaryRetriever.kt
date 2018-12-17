package com.cshep4.premierpredictor.matchupdate.component.update

import com.cshep4.premierpredictor.matchupdate.component.api.ApiRequester
import com.cshep4.premierpredictor.matchupdate.component.time.Time
import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommentaryRetriever: Retriever<Commentary> {
    @Autowired
    private lateinit var apiRequester: ApiRequester

    @Autowired
    private lateinit var time: Time

    override fun getLatest(id: String): Commentary? {
        val apiResult = apiRequester.retrieveCommentary(id) ?: return null
        apiResult.lastUpdated = time.localDateTimeNow()

        return apiResult
    }
}