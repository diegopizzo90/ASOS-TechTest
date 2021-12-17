package com.diegopizzo.network.service

import com.diegopizzo.network.model.*
import com.diegopizzo.network.service.NetworkConstant.COMPANY_INFO_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.LAUNCHES_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.ROCKET_ENDPOINT
import com.diegopizzo.network.testutil.enqueueResponse
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {
    private lateinit var server: MockWebServer

    private lateinit var api: ApiService

    @Before
    fun setUp() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url(BASE_URL))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun after() {
        server.close()
    }

    @Test
    fun getCompanyInfo_recordRequest_assertEqualsTrue() {
        server.enqueueResponse("company_info_success_response.json", 200)
        api.getCompanyInfo().blockingGet()

        val request = server.takeRequest()
        assertEquals(request.path, BASE_URL + COMPANY_INFO_ENDPOINT)
    }

    @Test
    fun getCompanyInfo_successResponse_assertEqualsTrue() {
        server.enqueueResponse("company_info_success_response.json", 200)
        val response = api.getCompanyInfo().blockingGet()

        assertEquals(expectedCompanyInfo, response.body())
    }

    @Test
    fun getCompanyInfo_serverErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(500))
        val response = api.getCompanyInfo().blockingGet()
        assertEquals(null, response.body())
    }

    @Test
    fun getCompanyInfo_clientErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(400))
        val response = api.getCompanyInfo().blockingGet()
        assertEquals(null, response.body())
    }

    @Test
    fun getLaunches_recordRequest_assertEqualsTrue() {
        server.enqueueResponse("launches_success_response.json", 200)
        api.getLaunches().blockingGet()

        val request = server.takeRequest()
        assertEquals(request.path, BASE_URL + LAUNCHES_ENDPOINT)
    }

    @Test
    fun getLaunches_successResponse_assertEqualsTrue() {
        server.enqueueResponse("launches_success_response.json", 200)
        val response = api.getLaunches().blockingGet()

        assertEquals(expectedLaunchesList, response.body())
    }

    @Test
    fun getLaunches_serverErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(500))
        val response = api.getLaunches().blockingGet()
        assertEquals(null, response.body())
    }

    @Test
    fun getLaunches_clientErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(400))
        val response = api.getLaunches().blockingGet()
        assertEquals(null, response.body())
    }

    @Test
    fun getRocketById_recordRequest_assertEqualsTrue() {
        server.enqueueResponse("rocket_success_response.json", 200)
        api.getRocketById(ROCKET_ID).blockingGet()

        val request = server.takeRequest()
        assertEquals(request.path, "$BASE_URL$ROCKET_ENDPOINT/$ROCKET_ID")
    }

    @Test
    fun getRocketById_successResponse_assertEqualsTrue() {
        server.enqueueResponse("rocket_success_response.json", 200)
        val response = api.getRocketById(ROCKET_ID).blockingGet()

        assertEquals(expectedRocket, response.body())
    }

    @Test
    fun getRocketById_serverErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(500))
        val response = api.getRocketById(ROCKET_ID).blockingGet()
        assertEquals(null, response.body())
    }

    @Test
    fun getRocketById_clientErrorResponse_assertEqualsTrue() {
        server.enqueue(MockResponse().setResponseCode(400))
        val response = api.getRocketById(ROCKET_ID).blockingGet()
        assertEquals(null, response.body())
    }

    companion object {
        private const val BASE_URL = "/"
        private const val ROCKET_ID = "1234455"

        private val expectedCompanyInfo =
            CompanyInfo("SpaceX", "Elon Musk", 2002, 9500, 3, 74000000000)

        private val expectedLaunchesList = listOf(
            Launch(
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "5e9d0d95eda69955f709d1eb",
                success = false,
                upcoming = false,
                Links(
                    "https://en.wikipedia.org/wiki/DemoSat",
                    Patch("https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png")
                )
            ),
            Launch(
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "5e9d0d95eda69973a809d1ec",
                success = true,
                upcoming = false,
                Links(
                    "https://en.wikipedia.org/wiki/SES-12",
                    Patch("https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png")
                )
            ),
            Launch(
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "5e9d0d95eda69955f709d1eb",
                success = false,
                upcoming = false,
                Links(
                    "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                    Patch("https://images2.imgbox.com/3d/86/cnu0pan8_o.png")
                )
            ),
            Launch(
                "NROL-87",
                "2022-02-02T00:00:00.000Z",
                "5e9d0d95eda69973a809d1ec",
                success = null,
                upcoming = true,
                Links(null, Patch(null))
            )
        )

        private val expectedRocket = Rocket("Falcon 9", Engines("merlin"))
    }
}