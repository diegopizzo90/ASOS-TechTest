package com.diegopizzo.network.model

import com.google.gson.annotations.SerializedName

internal data class CompanyInfo(
    val name: String?,
    val founder: String?,
    val founded: Int?,
    val employees: Int?,
    @SerializedName("launch_sites") val launchSites: Int? = 0,
    val valuation: Long?
)

internal data class Patch(val small: String?)
internal data class Links(val wikipedia: String?, val patch: Patch?)
internal data class Launch(
    val name: String,
    @SerializedName("date_utc") val dateUtc: String,
    val rocket: String,
    val success: Boolean?,
    val upcoming: Boolean,
    val links: Links?,
)

internal data class Engines(val type: String?)
internal data class Rocket(val name: String?, val engines: Engines?)

data class SpaceXDataModel(
    val companyInfo: CompanyInfoDataModel?,
    val launches: List<LaunchDataModel>?
)

data class CompanyInfoDataModel(
    val companyName: String? = null,
    val founder: String? = null,
    val founded: String? = null,
    val employees: String? = null,
    val launchSites: String? = null,
    val valuation: String? = null
)

data class LaunchDataModel(
    val missionName: String,
    val dateTimeUtc: String,
    val rocketName: String?,
    val rocketType: String?,
    val isSuccess: Boolean?,
    val isUpcoming: Boolean,
    val wikipediaLink: String?,
    val patchImage: String?
)