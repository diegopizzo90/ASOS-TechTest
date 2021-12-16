package com.diegopizzo.network.interactor

import com.diegopizzo.network.cache.ISpaceXCacheInteractor
import com.diegopizzo.network.creator.SpaceXCreator
import com.diegopizzo.network.model.*
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class SpaceXInteractorTest {

    private lateinit var interactor: ISpaceXInteractor

    @Mock
    private lateinit var cache: ISpaceXCacheInteractor

    @Before
    fun setUp() {
        interactor = SpaceXInteractor(cache, SpaceXCreator())
    }

    @Test
    fun getSpaceXInfo_successResult_assertEqualsTrue() {
        `when`(cache.getRocketById(anyString())).thenReturn(Single.just(rocketSuccess))
        `when`(cache.getLaunches()).thenReturn(Single.just(launchesSuccess))
        `when`(cache.getCompanyInfo()).thenReturn(Single.just(companyInfoSuccess))

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoResult, actualValue)
    }

    @Test
    fun getSpaceXInfo_serverErrorOnLaunchesEndpoint_assertEqualsTrue() {
        `when`(cache.getLaunches()).thenReturn(
            Single.just(Response.error(500, "body".toResponseBody()))
        )
        `when`(cache.getCompanyInfo()).thenReturn(Single.just(companyInfoSuccess))

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutLaunches, actualValue)
    }

    @Test
    fun getSpaceXInfo_clientErrorOnLaunchesEndpoint_assertEqualsTrue() {
        `when`(cache.getLaunches()).thenReturn(
            Single.just(Response.error(400, "body".toResponseBody()))
        )
        `when`(cache.getCompanyInfo()).thenReturn(Single.just(companyInfoSuccess))

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutLaunches, actualValue)
    }

    @Test
    fun getSpaceXInfo_serverErrorOnCompanyInfoEndpoint_assertEqualsTrue() {
        `when`(cache.getRocketById(anyString())).thenReturn(Single.just(rocketSuccess))
        `when`(cache.getLaunches()).thenReturn(Single.just(launchesSuccess))
        `when`(cache.getCompanyInfo()).thenReturn(
            Single.just(Response.error(500, "body".toResponseBody()))
        )

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutCompanyInfo, actualValue)
    }

    @Test
    fun getSpaceXInfo_clientErrorOnCompanyInfoEndpoint_assertEqualsTrue() {
        `when`(cache.getRocketById(anyString())).thenReturn(Single.just(rocketSuccess))
        `when`(cache.getLaunches()).thenReturn(Single.just(launchesSuccess))
        `when`(cache.getCompanyInfo()).thenReturn(
            Single.just(Response.error(400, "body".toResponseBody()))
        )

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutCompanyInfo, actualValue)
    }

    @Test
    fun getSpaceXInfo_serverErrorOnRocketInfoEndpoint_assertEqualsTrue() {
        `when`(cache.getRocketById(anyString())).thenReturn(
            Single.just(
                Response.error(
                    500,
                    "body".toResponseBody()
                )
            )
        )
        `when`(cache.getLaunches()).thenReturn(Single.just(launchesSuccess))
        `when`(cache.getCompanyInfo()).thenReturn(Single.just(companyInfoSuccess))

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutRocketDetails, actualValue)
    }

    @Test
    fun getSpaceXInfo_clientErrorOnRocketInfoEndpoint_assertEqualsTrue() {
        `when`(cache.getRocketById(anyString())).thenReturn(
            Single.just(
                Response.error(
                    500,
                    "body".toResponseBody()
                )
            )
        )
        `when`(cache.getLaunches()).thenReturn(Single.just(launchesSuccess))
        `when`(cache.getCompanyInfo()).thenReturn(Single.just(companyInfoSuccess))

        val actualValue = interactor.getSpaceXInfo().blockingGet()
        assertEquals(expectedSpaceXInfoWithoutRocketDetails, actualValue)
    }

    companion object {
        private val companyInfoSuccess =
            Response.success(CompanyInfo("SpaceX", "Elon Musk", 2002, 9500, 3, 74000000000))

        private val launchesSuccess = Response.success(
            listOf(
                Launch(
                    "FalconSat",
                    "2006-03-24T22:30:00.000Z",
                    "5e9d0d95eda69955f709d1eb",
                    success = false,
                    upcoming = false,
                    Links("https://en.wikipedia.org/wiki/DemoSat")
                ),
                Launch(
                    "SES-12",
                    "2018-06-04T04:45:00.000Z",
                    "5e9d0d95eda69973a809d1ec",
                    success = true,
                    upcoming = false,
                    Links("https://en.wikipedia.org/wiki/SES-12")
                ),
                Launch(
                    "Trailblazer",
                    "2008-08-03T03:34:00.000Z",
                    "5e9d0d95eda69955f709d1eb",
                    success = false,
                    upcoming = false,
                    Links("https://en.wikipedia.org/wiki/Trailblazer_(satellite)")
                ),
                Launch(
                    "NROL-87",
                    "2022-02-02T00:00:00.000Z",
                    "5e9d0d95eda69973a809d1ec",
                    success = null,
                    upcoming = true,
                    Links(null)
                )
            )
        )

        private val rocketSuccess = Response.success(Rocket("RocketName", Engines("RocketType")))

        private val expectedLaunchesDataModel = listOf(
            LaunchDataModel(
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat"
            ),
            LaunchDataModel(
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12"
            ),
            LaunchDataModel(
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)"
            ),
            LaunchDataModel(
                "NROL-87",
                "2022-02-02T00:00:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = null,
                isUpcoming = true,
                null
            )
        )
        private val companyInfoDataModel =
            CompanyInfoDataModel("SpaceX", "Elon Musk", "2002", "9500", "3", "74000000000")

        private val expectedSpaceXInfoResult = SpaceXDataModel(
            companyInfoDataModel,
            expectedLaunchesDataModel
        )

        private val expectedSpaceXInfoWithoutLaunches =
            SpaceXDataModel(companyInfoDataModel, emptyList())

        private val emptyCompanyInfoDataModel = CompanyInfoDataModel()

        private val expectedSpaceXInfoWithoutCompanyInfo =
            SpaceXDataModel(emptyCompanyInfoDataModel, expectedLaunchesDataModel)

        private val expectedLaunchesDataModelWithoutRocketDetails = listOf(
            LaunchDataModel(
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                null,
                null,
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat"
            ),
            LaunchDataModel(
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                null,
                null,
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12"
            ),
            LaunchDataModel(
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                null,
                null,
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)"
            ),
            LaunchDataModel(
                "NROL-87",
                "2022-02-02T00:00:00.000Z",
                null,
                null,
                isSuccess = null,
                isUpcoming = true,
                null
            )
        )

        private val expectedSpaceXInfoWithoutRocketDetails =
            SpaceXDataModel(companyInfoDataModel, expectedLaunchesDataModelWithoutRocketDetails)
    }
}