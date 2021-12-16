package com.diegopizzo.network.interactor

import com.diegopizzo.network.base.BaseInteractor
import com.diegopizzo.network.base.Result
import com.diegopizzo.network.cache.ISpaceXCacheInteractor
import com.diegopizzo.network.creator.ISpaceXCreator
import com.diegopizzo.network.model.*
import io.reactivex.Single
import io.reactivex.functions.BiFunction

internal class SpaceXInteractor(
    private val cache: ISpaceXCacheInteractor,
    private val creator: ISpaceXCreator
) : ISpaceXInteractor, BaseInteractor() {

    override fun getSpaceXInfo(): Single<SpaceXDataModel> {
        val zipper =
            BiFunction<CompanyInfoDataModel, List<LaunchDataModel>, SpaceXDataModel> { companyInfoResult, launches ->
                creator.fromModelToDataModel(companyInfoResult, launches)
            }

        return Single.zip(getCompanyInfo(), getLaunches(), zipper)
    }

    private fun getLaunches(): Single<List<LaunchDataModel>> {
        return cache.getLaunches().flatMap {
            when (val result = handleResponse(it)) {
                is Result.Success -> mergeListOfLaunchesWithRocketDetails(result.data)
                is Result.Error -> mergeListOfLaunchesWithRocketDetails(emptyList())
            }
        }
    }

    private fun mergeListOfLaunchesWithRocketDetails(launches: List<Launch>?): Single<List<LaunchDataModel>>? {
        return Single.merge(getLaunchesWithRocketDetailsAdded(launches)).toList()
    }

    private fun getCompanyInfo(): Single<CompanyInfoDataModel> {
        return cache.getCompanyInfo().map {
            when (val result = handleResponse(it)) {
                is Result.Success -> creator.fromCompanyInfoToDataModel(result.data)
                is Result.Error -> creator.fromCompanyInfoToDataModel(null)
            }
        }
    }

    private fun getLaunchesWithRocketDetailsAdded(launches: List<Launch>?): List<Single<LaunchDataModel>>? {
        return launches?.map { launch -> getSingleLaunchWithRocketDetails(launch.rocket, launch) }
    }

    private fun getSingleLaunchWithRocketDetails(
        rocketId: String,
        launch: Launch
    ): Single<LaunchDataModel> {
        return getRocketById(rocketId).map { result ->
            when (result) {
                is Result.Success -> creator.fromLaunchesModelToDataModel(launch, result.data)
                is Result.Error -> creator.fromLaunchesModelToDataModel(launch, null)
            }
        }
    }

    private fun getRocketById(id: String): Single<Result<Rocket>> {
        return cache.getRocketById(id).map { handleResponse(it) }
    }
}