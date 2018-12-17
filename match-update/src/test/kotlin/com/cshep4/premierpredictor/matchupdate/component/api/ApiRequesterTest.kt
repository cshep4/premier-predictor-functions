package com.cshep4.premierpredictor.matchupdate.component.api

import com.cshep4.premierpredictor.matchupdate.data.api.live.commentary.Commentary
import com.cshep4.premierpredictor.matchupdate.data.api.live.match.MatchFacts
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.test.util.ReflectionTestUtils.setField
import org.hamcrest.CoreMatchers.`is` as Is

internal class ApiRequesterTest {
    companion object {
        const val URL = "https://www.test.com/"
        const val API_KEY = "1234"
    }

    private val client: Client = mockk()
    private val apiRequester = ApiRequester()

    @Before
    fun init() {
        FuelManager.instance.client = client

        setField(apiRequester, "apiUrl", URL)
        setField(apiRequester, "apiKey", API_KEY)
        setField(apiRequester, "apiCommentaryUrl", URL)
    }

    @Test
    fun `'retrieveFixtures' parses API result and returns object`() {
        val matches = listOf(MatchFacts(localTeamScore = "", visitorTeamScore = ""), MatchFacts(localTeamScore = "", visitorTeamScore = ""))
        val jsonResponse = ObjectMapper().writeValueAsString(matches)

        every { client.executeRequest(any()).statusCode } returns 200
        every { client.executeRequest(any()).responseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = apiRequester.retrieveFixtures()

        assertThat(fixturesApiResult, Is(matches))
    }

    @Test
    fun `'retrieveFixtures' returns empty list if API does not return OK`() {
        every { client.executeRequest(any()).statusCode } returns 500
        every { client.executeRequest(any()).responseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveFixtures()

        assertThat(fixturesApiResult, Is(emptyList()))
    }

    @Test
    fun `'retrieveCommentary' retrieves commentary and returns in object form`() {
        val commentary = Commentary()
        val jsonResponse = ObjectMapper().writeValueAsString(commentary)

        every { client.executeRequest(any()).statusCode } returns 200
        every { client.executeRequest(any()).responseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(commentary))
    }

    @Test
    fun `'retrieveCommentary' returns null if commentary is not found`() {
        val json = "{ \"status\": \"error\",\"message\": \"We did not find commentaries for the provided match\",\"code\": 404 }"

        every { client.executeRequest(any()).statusCode } returns 404
        every { client.executeRequest(any()).responseMessage } returns "Not Found"
        every { client.executeRequest(any()).data } returns json.toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(nullValue()))
    }

    @Test
    fun `'retrieveCommentary' returns null if there is an error`() {
        every { client.executeRequest(any()).statusCode } returns 500
        every { client.executeRequest(any()).responseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveCommentary("1")

        assertThat(fixturesApiResult, Is(nullValue()))
    }

    @Test
    fun `'retrieveMatch' parses API result and returns object`() {
        val match = MatchFacts()
        val jsonResponse = ObjectMapper().writeValueAsString(match)

        every { client.executeRequest(any()).statusCode } returns 200
        every { client.executeRequest(any()).responseMessage } returns "OK"
        every { client.executeRequest(any()).data } returns jsonResponse.toByteArray()

        val apiResult = apiRequester.retrieveMatch("1")

        assertThat(apiResult, Is(match))
    }

    @Test
    fun `'retrieveMatch' returns null if API does not return OK`() {
        every { client.executeRequest(any()).statusCode } returns 500
        every { client.executeRequest(any()).responseMessage } returns "Internal Server Error"
        every { client.executeRequest(any()).data } returns "".toByteArray()

        val fixturesApiResult = apiRequester.retrieveMatch("1")

        assertThat(fixturesApiResult, Is(nullValue()))
    }
}