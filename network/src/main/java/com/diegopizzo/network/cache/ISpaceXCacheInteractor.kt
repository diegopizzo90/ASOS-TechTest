package com.diegopizzo.network.cache

import com.diegopizzo.network.model.CompanyInfo
import com.diegopizzo.network.model.Launch
import com.diegopizzo.network.model.Rocket
import io.reactivex.Single
import retrofit2.Response

internal interface ISpaceXCacheInteractor {
    fun getCompanyInfo(): Single<Response<CompanyInfo>>
    fun getLaunches(): Single<Response<List<Launch>>>
    fun getRocketById(id: String): Single<Response<Rocket>>
}
