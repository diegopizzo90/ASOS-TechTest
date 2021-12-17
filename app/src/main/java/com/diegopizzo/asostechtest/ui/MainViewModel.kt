package com.diegopizzo.asostechtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private var _viewState: MainViewState? = null
    var viewState: MainViewState
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value
        }

    init {
        viewState = MainViewState()
    }

    fun getSpaceXInformation() {
        disposables.add(
            interactor.getSpaceXInfo()
                .subscribeOn(subscriberScheduler)
                .observeOn(observerScheduler)
                .doOnSubscribe {
                    viewState = viewState.copy(isLoading = true, isLaunchesNotAvailable = false)
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
                        isLaunchesNotAvailable = true
                    )
                })
        )
    }

    fun openArticle(articleLink: String?) {
        viewState = viewState.copy(isItemClicked = true, launchArticle = articleLink)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

data class MainViewState(
    val spaceXInformation: SpaceXDataModel? = null,
    val isLoading: Boolean? = null,
    val isLaunchesNotAvailable: Boolean? = null,
    val isItemClicked: Boolean? = null,
    val launchArticle: String? = null
)