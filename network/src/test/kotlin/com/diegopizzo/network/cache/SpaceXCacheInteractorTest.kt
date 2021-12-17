package com.diegopizzo.network.cache

import com.diegopizzo.network.model.CompanyInfo
import com.diegopizzo.network.model.Launch
import com.diegopizzo.network.model.Rocket
import com.diegopizzo.network.service.ApiService
import com.diegopizzo.network.service.NetworkConstant.COMPANY_INFO_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.LAUNCHES_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.ROCKET_ENDPOINT
import com.diegopizzo.network.testutil.enqueueResponse
import com.google.gson.Gson
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class SpaceXCacheInteractorTest {

    private lateinit var server: MockWebServer

    private lateinit var api: ApiService

    private lateinit var cache: ISpaceXCacheInteractor

    private lateinit var observerCompanyInfo: TestObserver<Response<CompanyInfo>>
    private lateinit var observerLaunches: TestObserver<Response<List<Launch>>>
    private lateinit var observerRocket: TestObserver<Response<Rocket>>


    @Before
    fun setUp() {
        server = MockWebServer()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiService::class.java)
        cache = SpaceXCacheInteractor(api, 0L)

        observerCompanyInfo = TestObserver()
        observerLaunches = TestObserver()
        observerRocket = TestObserver()
    }

    @After
    fun shutDown() {
        server.shutdown()
    }

    @Test
    fun getCompanyInfo_recordedRequestAndCached_assertEqualsTrue() {
        server.enqueueResponse("company_info_success_response.json", 200)
        //First request, cache empty response retrieved from the API and saved in the cache
        cache.getCompanyInfo().subscribe(observerCompanyInfo)
        val request1 = server.takeRequest(300, TimeUnit.MILLISECONDS)
        assertEquals(request1?.path, BASE_URL + COMPANY_INFO_ENDPOINT)

        //Second request, cache not empty response retrieved from the cache. API not called
        cache.getCompanyInfo().subscribe(observerCompanyInfo)
        val request2 = server.takeRequest(200, TimeUnit.MILLISECONDS)
        assertEquals(request2?.path, null)
    }

    @Test
    fun getLaunches_recordedRequestAndCached_assertEqualsTrue() {
        server.enqueueResponse("launches_success_response.json", 200)
        //First request, cache empty response retrieved from the API and saved in the cache
        cache.getLaunches().subscribe(observerLaunches)
        val request1 = server.takeRequest(200, TimeUnit.MILLISECONDS)
        assertEquals(request1?.path, BASE_URL + LAUNCHES_ENDPOINT)

        //Second request, cache not empty response retrieved from the cache. API not called
        cache.getLaunches().subscribe(observerLaunches)
        val request2 = server.takeRequest(200, TimeUnit.MILLISECONDS)
        assertEquals(request2?.path, null)
    }

    @Test
    fun getRocket_recordedRequestAndCached_assertEqualsTrue() {
        server.enqueueResponse("rocket_success_response.json", 200)
        //First request, cache empty response retrieved from the API and saved in the cache
        cache.getRocketById(ROCKET_ID).subscribe(observerRocket)
        val request1 = server.takeRequest(200, TimeUnit.MILLISECONDS)
        assertEquals(request1?.path, "$BASE_URL$ROCKET_ENDPOINT/$ROCKET_ID")

        //Second request, cache not empty response retrieved from the cache. API not called
        cache.getRocketById(ROCKET_ID).subscribe(observerRocket)
        val request2 = server.takeRequest(200, TimeUnit.MILLISECONDS)
        assertEquals(request2?.path, null)
    }

    companion object {
        private const val BASE_URL = "/"
        private const val ROCKET_ID = "1233"
    }
}