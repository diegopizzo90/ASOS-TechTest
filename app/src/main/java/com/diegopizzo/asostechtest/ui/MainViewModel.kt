package com.diegopizzo.asostechtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diegopizzo.asostechtest.ui.FilterObject.LaunchOutcome
import com.diegopizzo.asostechtest.ui.FilterObject.Sorting
import com.diegopizzo.asostechtest.ui.ViewEffect.ShowLaunchArticle
import com.diegopizzo.asostechtest.ui.util.SingleLiveEvent
import com.diegopizzo.network.interactor.ISpaceXInteractor
import com.diegopizzo.network.model.SpaceXDataModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val interactor: ISpaceXInteractor,
    private val disposables: CompositeDisposable,
    private val subscriberScheduler: Scheduler = Schedulers.io(),
    private val observerScheduler: Scheduler = AndroidSchedulers.mainThread()
) : ViewModel() {

    private val _viewStates: MutableLiveData<MainViewState> = MutableLiveData()
    val viewStates: LiveData<MainViewState> = _viewStates

    private val _viewEffects: SingleLiveEvent<ViewEffect> = SingleLiveEvent()
    fun viewEffects(): SingleLiveEvent<ViewEffect> = _viewEffects

    private var _viewState: MainViewState? = null
    var viewState: MainViewState
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value
        }

    private val _filterViewStates: MutableLiveData<FilterDialogViewState> = MutableLiveData()
    val filterViewStates: LiveData<FilterDialogViewState> = _filterViewStates

    private var _filterViewState: FilterDialogViewState? = null
    var filterViewState: FilterDialogViewState
        get() = _filterViewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _filterViewState = value
            _filterViewStates.value = value
        }

    init {
        viewState = MainViewState()
        filterViewState = FilterDialogViewState()
    }

    fun getSpaceXInformation(isFresh: Boolean = false) {
        disposables.add(
            interactor.getSpaceXInfo()
                .subscribeOn(subscriberScheduler)
                .observeOn(observerScheduler)
                .doOnSubscribe {
                    viewState = viewState.copy(isLoading = true, isDataNotAvailable = false)
                }
                .subscribe({
                    viewState = viewState.copy(
                        spaceXInformation = it,
                        isLoading = false,
                        isLaunchesNotAvailable = it.launches.isNullOrEmpty()
                    )
                }, {
                    viewState = viewState.copy(
                        spaceXInformation = null,
                        isLoading = false,
                        isDataNotAvailable = true
                    )
                })
        )
    }


    fun openArticle(articleLink: String?) {
        viewEffects().value = ShowLaunchArticle(articleLink)
    }

    fun onSortingSelected(sorting: Pair<Sorting?, Int>) {
        filterViewState =
            filterViewState.copy(sortingSelected = sorting, isFilterPerforming = false)
    }

    fun onLaunchOutcomeFilterSelected(launchOutcome: Pair<LaunchOutcome?, Int>) {
        filterViewState = filterViewState.copy(
            launchOutcomeFilterSelected = launchOutcome,
            isFilterPerforming = false
        )
    }

    fun onFromYearFilterSelected(year: Int?) {
        filterViewState =
            filterViewState.copy(fromYearFilterSelected = year, isFilterPerforming = false)
    }

    fun onToYearFilterSelected(year: Int?) {
        filterViewState =
            filterViewState.copy(toYearFilterSelected = year, isFilterPerforming = false)
    }

    fun performFilter() {
        val oldQuery = filterViewState.queryFilter
        val newQuery = getFilterQuery()
        if (oldQuery != newQuery) {
            filterViewState =
                filterViewState.copy(queryFilter = newQuery, isFilterPerforming = true)
        }
    }

    private fun getFilterQuery(): FilterObject {
        filterViewState.apply {
            return FilterObject(
                fromYearFilterSelected,
                toYearFilterSelected,
                launchOutcomeFilterSelected.first,
                sortingSelected.first
            )
        }
    }

    fun retry() {
        getSpaceXInformation(isFresh = true)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

sealed class ViewEffect {
    data class ShowLaunchArticle(val articleLink: String?) : ViewEffect()
}

data class MainViewState(
    val spaceXInformation: SpaceXDataModel? = null,
    val isLoading: Boolean = false,
    val isDataNotAvailable: Boolean = false,
    val isLaunchesNotAvailable: Boolean = false,
)

data class FilterDialogViewState(
    val fromYearFilterSelected: Int? = null,
    val toYearFilterSelected: Int? = null,
    val launchOutcomeFilterSelected: Pair<LaunchOutcome?, Int> = Pair(null, 0),//(Filter value selected, position in multiButtonView)
    val sortingSelected: Pair<Sorting?, Int> = Pair(null, 0),//(Filter value selected, position in multiButtonView)
    val queryFilter: FilterObject = FilterObject(),
    val isFilterPerforming: Boolean = false
)

data class FilterObject(
    val fromYear: Int? = null,
    val toYear: Int? = null,
    val launchOutcome: LaunchOutcome? = null,
    val sorting: Sorting? = null
) {
    enum class Sorting {
        ASC, DESC
    }

    enum class LaunchOutcome {
        SUCCESS_LAUNCH, FAILED_LAUNCH
    }
}