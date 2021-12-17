package com.diegopizzo.network.creator

import com.diegopizzo.network.model.*

internal class SpaceXCreator : ISpaceXCreator {

    override fun fromModelToDataModel(
        companyInfo: CompanyInfoDataModel?,
        launches: List<LaunchDataModel>?,
    ): SpaceXDataModel {
        return SpaceXDataModel(companyInfo, launches)
    }

    override fun fromCompanyInfoToDataModel(companyInfo: CompanyInfo?): CompanyInfoDataModel {
        return CompanyInfoDataModel(
            companyInfo?.name,
            companyInfo?.founder,
            companyInfo?.founded?.toString(),
            companyInfo?.employees?.toString(),
            companyInfo?.launchSites?.toString(),
            companyInfo?.valuation?.toString()
        )
    }

    override fun fromLaunchesModelToDataModel(launch: Launch, rocket: Rocket?): LaunchDataModel {
        return LaunchDataModel(
            launch.name,
            launch.dateUtc,
            rocket?.name,
            rocket?.engines?.type,
            launch.success,
            launch.upcoming,
            launch.links?.wikipedia,
            launch.links?.patch?.small
        )
    }
}