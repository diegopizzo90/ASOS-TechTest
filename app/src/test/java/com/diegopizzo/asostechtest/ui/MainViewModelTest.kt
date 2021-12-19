package com.diegopizzo.asostechtest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.diegopizzo.asostechtest.ui.FilterObject.LaunchOutcome
import com.diegopizzo.asostechtest.ui.FilterObject.Sorting
import com.diegopizzo.network.interactor.ISpaceXInteractor
import com.diegopizzo.network.model.CompanyInfoDataModel
import com.diegopizzo.network.model.LaunchDataModel
import com.diegopizzo.network.model.SpaceXDataModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var interactor: ISpaceXInteractor

    @Mock
    private lateinit var mainViewStateObserver: Observer<MainViewState>

    @Mock
    private lateinit var filterViewStateObserver: Observer<FilterDialogViewState>

    @Mock
    private lateinit var viewEffectObserver: Observer<ViewEffect>

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            interactor, CompositeDisposable(), Schedulers.trampoline(), Schedulers.trampoline()
        ).apply {
            viewStates.observeForever(mainViewStateObserver)
            filterViewStates.observeForever(filterViewStateObserver)
            viewEffects().observeForever(viewEffectObserver)
        }
    }

    @Test
    fun getSpaceXInfo_successResult_verifyState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.just(successResult))

        viewModel.getSpaceXInformation()

        //Loading
        verify(mainViewStateObserver).onChanged(
            MainViewState(
                isLoading = true,
                isDataNotAvailable = false
            )
        )
        //Final result
        verify(mainViewStateObserver).onChanged(
            MainViewState(
                spaceXInformation = successResult,
                isLoading = false,
                isDataNotAvailable = false
            )
        )
    }

    @Test
    fun getSpaceXInfo_emptyLaunchesResult_verifyState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.just(emptyLaunchesResult))

        viewModel.getSpaceXInformation()

        //Loading
        verify(mainViewStateObserver).onChanged(
            MainViewState(
                isLoading = true,
                isLaunchesNotAvailable = false
            )
        )
        //Final result
        verify(mainViewStateObserver).onChanged(
            MainViewState(
                spaceXInformation = emptyLaunchesResult,
                isLoading = false,
                isLaunchesNotAvailable = true
            )
        )
    }

    @Test
    fun getSpaceXInfo_errorResult_verifyState() {
        `when`(interactor.getSpaceXInfo()).thenReturn(Single.error(Exception("any error")))

        viewModel.getSpaceXInformation()

        verify(mainViewStateObserver).onChanged(
            MainViewState(
                spaceXInformation = null,
                isLoading = false,
                isDataNotAvailable = true
            )
        )
    }

    @Test
    fun openLaunchArticle_onEventPerformed_verifyState() {
        viewModel.openArticle("article")

        verify(viewEffectObserver).onChanged(
            ViewEffect.ShowLaunchArticle(articleLink = "article")
        )
    }

    @Test
    fun onSortingSelected_onEventPerformed_verifyState() {
        viewModel.onSortingSelected(Pair(Sorting.ASC, 1))

        verify(filterViewStateObserver).onChanged(
            FilterDialogViewState(
                sortingSelected = Pair(Sorting.ASC, 1),
                isFilterPerforming = false
            )
        )
    }

    @Test
    fun onLaunchOutcomeFilterSelected_onEventPerformed_verifyState() {
        viewModel.onLaunchOutcomeFilterSelected(Pair(LaunchOutcome.SUCCESS_LAUNCH, 2))

        verify(filterViewStateObserver).onChanged(
            FilterDialogViewState(
                launchOutcomeFilterSelected = Pair(
                    LaunchOutcome.SUCCESS_LAUNCH,
                    2
                ), isFilterPerforming = false
            )
        )
    }

    @Test
    fun onFromYearFilterSelected_onEventPerformed_verifyState() {
        viewModel.onFromYearFilterSelected(2018)

        verify(filterViewStateObserver).onChanged(
            FilterDialogViewState(fromYearFilterSelected = 2018)
        )
    }

    @Test
    fun onToYearFilterSelected_onEventPerformed_verifyState() {
        viewModel.onToYearFilterSelected(2098)

        verify(filterViewStateObserver).onChanged(
            FilterDialogViewState(toYearFilterSelected = 2098)
        )
    }

    @Test
    fun performFilter_onEventPerformed_verifyState() {
        viewModel.onSortingSelected(Pair(Sorting.ASC, 1))
        viewModel.onToYearFilterSelected(2098)
        viewModel.performFilter()

        verify(filterViewStateObserver).onChanged(
            FilterDialogViewState(
                toYearFilterSelected = 2098,
                sortingSelected = Pair(Sorting.ASC, 1),
                queryFilter = FilterObject(sorting = Sorting.ASC, toYear = 2098),
                isFilterPerforming = true
            )
        )
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

        private val emptyLaunchesResult =
            SpaceXDataModel(companyInfoDataModel, emptyList())
    }
}