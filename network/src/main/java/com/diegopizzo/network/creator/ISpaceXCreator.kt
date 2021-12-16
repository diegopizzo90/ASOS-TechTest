package com.diegopizzo.network.creator

import com.diegopizzo.network.model.*

internal interface ISpaceXCreator {

    fun fromModelToDataModel(
        companyInfo: CompanyInfoDataModel?,
        launches: List<LaunchDataModel>?
    ): SpaceXDataModel

    fun fromLaunchesModelToDataModel(launch: Launch, rocket: Rocket?): LaunchDataModel
    fun fromCompanyInfoToDataModel(companyInfo: CompanyInfo?): CompanyInfoDataModel
}
