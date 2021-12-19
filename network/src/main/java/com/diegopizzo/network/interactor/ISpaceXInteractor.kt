package com.diegopizzo.network.interactor

import com.diegopizzo.network.model.SpaceXDataModel
import io.reactivex.Single

interface ISpaceXInteractor {
    fun getSpaceXInfo(isFresh: Boolean = false): Single<SpaceXDataModel>
}
