package com.diegopizzo.asostechtest.ui.util

import com.diegopizzo.network.model.LaunchDataModel
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterUtilsTest {

    @Test
    fun getListFilteredFromYearGreaterEqualsThan_listFiltered_assertEqualsTrue() {
        val list = FilterUtils.getListFilteredFromYearGreaterEqualsThan(launchesUnfiltered, 2008)
        assertEquals(list, launchesFilteredGreaterThan)
    }

    @Test
    fun getListFilteredFromYearLowerEqualsThan_listFiltered_assertEqualsTrue() {
        val list = FilterUtils.getListFilteredFromYearLowerEqualsThan(launchesUnfiltered, 2008)
        assertEquals(list, launchesFilteredLowerThan)
    }

    @Test
    fun getListFilteredByLaunchOutcome_listFilteredSuccessLaunch_assertEqualsTrue() {
        val list = FilterUtils.getListFilteredByLaunchOutcome(launchesUnfiltered, true)
        assertEquals(list, launchesFilteredBySuccess)
    }

    @Test
    fun getListFilteredByLaunchOutcome_listFilteredFailedLaunch_assertEqualsTrue() {
        val list = FilterUtils.getListFilteredByLaunchOutcome(launchesUnfiltered, false)
        assertEquals(list, launchesFilteredByFailed)
    }

    @Test
    fun getListSorted_listFilteredAscending_assertEqualsTrue() {
        val list = FilterUtils.getListSorted(launchesUnfiltered, isAscending = true)
        assertEquals(list, launchesAscendingSorted)
    }

    @Test
    fun getListSorted_listFilteredDescending_assertEqualsTrue() {
        val list = FilterUtils.getListSorted(launchesUnfiltered, isAscending = false)
        assertEquals(list, launchesDescendingSorted)
    }

    companion object {
        private val launchesUnfiltered = listOf(
            LaunchDataModel(
                "5eb87cd9ffd86e000604b32a",
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = null,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat",
                "https://images2.imgbox.com/40/e3/GypSkayF_o.png"
            ),
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            )
        )

        val launchesFilteredLowerThan = listOf(
            LaunchDataModel(
                "5eb87cd9ffd86e000604b32a",
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = null,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat",
                "https://images2.imgbox.com/40/e3/GypSkayF_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            )
        )

        val launchesFilteredGreaterThan = listOf(
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            )
        )

        val launchesFilteredBySuccess = listOf(
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            )
        )

        val launchesFilteredByFailed = listOf<LaunchDataModel>()

        private val launchesAscendingSorted = listOf(
            LaunchDataModel(
                "5eb87cd9ffd86e000604b32a",
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = null,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat",
                "https://images2.imgbox.com/40/e3/GypSkayF_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            ),
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            )
        )

        private val launchesDescendingSorted = listOf(
            LaunchDataModel(
                "5eb87d1bffd86e000604b368",
                "SES-12",
                "2018-06-04T04:45:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/SES-12",
                "https://images2.imgbox.com/4b/b9/oS8ezl6V_o.png"
            ),
            LaunchDataModel(
                "5eb87cdbffd86e000604b32c",
                "Trailblazer",
                "2008-08-03T03:34:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = true,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/Trailblazer_(satellite)",
                "https://images2.imgbox.com/3d/86/cnu0pan8_o.png"
            ),
            LaunchDataModel(
                "5eb87cd9ffd86e000604b32a",
                "FalconSat",
                "2006-03-24T22:30:00.000Z",
                "RocketName",
                "RocketType",
                isSuccess = null,
                isUpcoming = false,
                "https://en.wikipedia.org/wiki/DemoSat",
                "https://images2.imgbox.com/40/e3/GypSkayF_o.png"
            )
        )
    }
}