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

    override fun getSpaceXInfo(isFresh: Boolean): Single<SpaceXDataModel> {
        val zipper =
            BiFunction<CompanyInfoDataModel, List<LaunchDataModel>, SpaceXDataModel> { companyInfoResult, launches ->
                creator.fromModelToDataModel(companyInfoResult, launches)
            }

        return Single.zip(getCompanyInfo(isFresh), getLaunches(isFresh), zipper)
    }

    private fun getLaunches(isFresh: Boolean): Single<List<LaunchDataModel>> {
        return cache.getLaunches(isFresh).flatMap {
            when (val result = handleResponse(it)) {
                is Result.Success -> mergeListOfLaunchesWithRocketDetails(result.data, isFresh)
                is Result.Error -> mergeListOfLaunchesWithRocketDetails(emptyList(), isFresh)
            }
        }
    }

    private fun mergeListOfLaunchesWithRocketDetails(
        launches: List<Launch>?,
        isFresh: Boolean
    ): Single<List<LaunchDataModel>>? {
        return Single.merge(getLaunchesWithRocketDetailsAdded(launches, isFresh)).toList()
    }

    private fun getCompanyInfo(isFresh: Boolean): Single<CompanyInfoDataModel> {
        return cache.getCompanyInfo(isFresh).map {
            when (val result = handleResponse(it)) {
                is Result.Success -> creator.fromCompanyInfoToDataModel(result.data)
                is Result.Error -> creator.fromCompanyInfoToDataModel(null)
            }
        }
    }

    private fun getLaunchesWithRocketDetailsAdded(
        launches: List<Launch>?,
        isFresh: Boolean
    ): List<Single<LaunchDataModel>>? {
        return launches?.map { launch ->
            getSingleLaunchWithRocketDetails(
                launch.rocket,
                launch,
                isFresh
            )
        }
    }

    private fun getSingleLaunchWithRocketDetails(
        rocketId: String,
        launch: Launch,
        isFresh: Boolean
    ): Single<LaunchDataModel> {
        return getRocketById(rocketId, isFresh).map { result ->
            when (result) {
                is Result.Success -> creator.fromLaunchesModelToDataModel(launch, result.data)
                is Result.Error -> creator.fromLaunchesModelToDataModel(launch, null)
            }
        }
    }

    private fun getRocketById(id: String, isFresh: Boolean): Single<Result<Rocket>> {
        return cache.getRocketById(id, isFresh).map { handleResponse(it) }
    }
}