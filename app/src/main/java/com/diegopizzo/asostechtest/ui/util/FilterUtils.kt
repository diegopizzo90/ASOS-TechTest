package com.diegopizzo.asostechtest.ui.util

import com.diegopizzo.asostechtest.util.Utils
import com.diegopizzo.network.base.isFalse
import com.diegopizzo.network.base.isTrue
import com.diegopizzo.network.model.LaunchDataModel

object FilterUtils {
    fun getListFilteredFromYearGreaterEqualsThan(
        list: List<LaunchDataModel>,
        year: Int
    ): List<LaunchDataModel> {
        return list.filter {
            Utils.isDateUtcGreaterEqualsThan(year, it.dateTimeUtc)
        }
    }

    fun getListFilteredFromYearLowerEqualsThan(
        list: List<LaunchDataModel>,
        year: Int
    ): List<LaunchDataModel> {
        return list.filter {
            Utils.isDateUtcLowerEqualsThan(year, it.dateTimeUtc)
        }
    }

    fun getListFilteredByLaunchOutcome(
        list: List<LaunchDataModel>,
        isSuccessOutcome: Boolean
    ): List<LaunchDataModel> {
        return list.filter {
            if (isSuccessOutcome) it.isSuccess.isTrue() else it.isSuccess.isFalse()
        }
    }

    fun getListSorted(list: List<LaunchDataModel>, isAscending: Boolean): List<LaunchDataModel> {
        return if (isAscending) {
            list.sortedBy {
                Utils.fromDateStringToZonedDateTimeUtc(it.dateTimeUtc)
            }
        } else {
            list.sortedByDescending {
                Utils.fromDateStringToZonedDateTimeUtc(it.dateTimeUtc)
            }
        }
    }
}