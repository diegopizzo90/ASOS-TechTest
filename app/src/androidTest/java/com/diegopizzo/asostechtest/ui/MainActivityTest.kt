package com.diegopizzo.asostechtest.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.*
import com.diegopizzo.asostechtest.R
import com.diegopizzo.network.interactor.ISpaceXInteractor
import com.diegopizzo.network.model.CompanyInfoDataModel
import com.diegopizzo.network.model.LaunchDataModel
import com.diegopizzo.network.model.SpaceXDataModel
import io.reactivex.Single
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Mock
    private lateinit var interactor: ISpaceXInteractor

    @Before
    fun setUp() {
        val interactorModule = module {
            factory { interactor }
        }
        loadKoinModules(interactorModule)
    }

    @Test
    fun activityLaunched_success_checkUiState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.just(successResult))
        scenario = launchActivity()
        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.company_title)).check(matches(isDisplayed()))
        onView(withId(R.id.launches_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_company_info)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("FalconSat"))))
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("SES-12"))))
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText("Trailblazer"))))

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_empty_launches)).check(matches(not(isDisplayed())))
    }

    @Test
    fun openLaunchArticle_onItemClicked_checkUiState() {
        init()
        activityLaunched_success_checkUiState()
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<MainAdapter.ViewHolder>(0, click())
        )

        intended(toPackage("com.android.chrome"))
    }

    @Test
    fun activityLaunched_errorResult_checkUiState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.error(Exception("any error")))
        scenario = launchActivity()
        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.company_title)).check(matches(isDisplayed()))
        onView(withId(R.id.launches_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_company_info)).check(matches(isDisplayed()))

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_empty_launches)).check(matches(not(isDisplayed())))

        onView(withId(R.id.tv_empty_data)).check(matches(isDisplayed()))
    }

    @Test
    fun activityLaunched_emptyResult_checkUiState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.just(emptyResult))
        scenario = launchActivity()
        onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
        onView(withId(R.id.company_title)).check(matches(isDisplayed()))
        onView(withId(R.id.launches_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_company_info)).check(matches(isDisplayed()))

        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_empty_data)).check(matches(not(isDisplayed())))

        onView(withId(R.id.tv_empty_launches)).check(matches(isDisplayed()))
    }


    @Test
    fun activityLaunched_showFilter_checkUiState() {
        activityLaunched_success_checkUiState()

        onView(withId(R.id.filterDialogFragment)).perform(click())

        onView(withId(R.id.btn_confirm)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_cancel)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_from_year_selected)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_from_year_selected_label)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_to_year_selected)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_to_year_selected_label)).check(matches(isDisplayed()))
        onView(withId(R.id.multi_button_launch_outcome)).check(matches(isDisplayed()))
        onView(withId(R.id.multi_button_sorting)).check(matches(isDisplayed()))
    }

    @Test
    fun filterShown_performFilter_checkUiState() {
        activityLaunched_showFilter_checkUiState()

        onView(withId(R.id.btn_confirm)).perform(click())

        onView(withId(R.id.btn_confirm)).check(doesNotExist())
        onView(withId(R.id.btn_cancel)).check(doesNotExist())
        onView(withId(R.id.tv_from_year_selected)).check(doesNotExist())
        onView(withId(R.id.tv_from_year_selected_label)).check(doesNotExist())
        onView(withId(R.id.tv_to_year_selected)).check(doesNotExist())
        onView(withId(R.id.tv_to_year_selected_label)).check(doesNotExist())
        onView(withId(R.id.multi_button_launch_outcome)).check(doesNotExist())
        onView(withId(R.id.multi_button_sorting)).check(doesNotExist())
    }

    companion object {
        private val companyInfoDataModel =
            CompanyInfoDataModel("SpaceX", "Elon Musk", "2002", "9500", "3", "74000000000")

        private val expectedLaunchesDataModel = listOf(
            LaunchDataModel(
                "5eb87cd9ffd86e000604b32a",
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat",
                "https://images2.imgbox.com/40/e3/GypSkayF_o.png"
            ),
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = false,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            )
        )

        private val successResult =
            SpaceXDataModel(companyInfoDataModel, expectedLaunchesDataModel)

        private val emptyResult =
            SpaceXDataModel(companyInfoDataModel, emptyList())
    }
}