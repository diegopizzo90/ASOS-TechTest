package com.diegopizzo.network.service

import com.diegopizzo.network.model.CompanyInfo
import com.diegopizzo.network.model.Launch
import com.diegopizzo.network.model.Rocket
import com.diegopizzo.network.service.NetworkConstant.COMPANY_INFO_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.LAUNCHES_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.ROCKET_ENDPOINT
import com.diegopizzo.network.service.NetworkConstant.ROCKET_ID
import com.diegopizzo.network.service.NetworkConstant.ROCKET_PATH_ID
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface ApiService {
    /**
     * API request SpaceX company information
     */
    @GET(COMPANY_INFO_ENDPOINT)
    fun getCompanyInfo(): Single<Response<CompanyInfo>>

    /**
     * API request that retrieves all SpaceX launches
     */
    @GET(LAUNCHES_ENDPOINT)
    fun getLaunches(): Single<Response<List<Launch>>>

    /**
     * API request that retrieves a rocket info by rocket id
     */
    @GET(ROCKET_ENDPOINT + ROCKET_PATH_ID)
    fun getRocketById(@Path(ROCKET_ID) rocketId: String): Single<Response<Rocket>>
}