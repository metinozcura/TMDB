package com.metinozcura.tmdb.common.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatExtTest {

    // region formatMoney
    @Test
    fun formatMoney_lessThanThousand_returnsDollarAmount() {
        assertEquals("$0", 0L.formatMoney())
        assertEquals("$1", 1L.formatMoney())
        assertEquals("$999", 999L.formatMoney())
    }

    @Test
    fun formatMoney_thousands_returnsKFormat() {
        assertEquals("$1.0K", 1_000L.formatMoney())
        assertEquals("$1.5K", 1_500L.formatMoney())
        assertEquals("$999.0K", 999_000L.formatMoney())
    }

    @Test
    fun formatMoney_millions_returnsMFormat() {
        assertEquals("$1.0M", 1_000_000L.formatMoney())
        assertEquals("$1.5M", 1_500_000L.formatMoney())
        assertEquals("$150.0M", 150_000_000L.formatMoney())
    }

    @Test
    fun formatMoney_boundaryValues_returnsCorrectFormat() {
        assertEquals("$999", 999L.formatMoney())
        assertEquals("$1.0K", 1_000L.formatMoney())
        assertEquals("$999.0K", 999_000L.formatMoney())
        assertEquals("$1.0M", 1_000_000L.formatMoney())
    }
    // endregion

    // region formatReleaseDate
    @Test
    fun formatReleaseDate_validIsoDate_returnsFormattedDate() {
        assertEquals("Jan 15, 2024", "2024-01-15".formatReleaseDate())
        assertEquals("Dec 31, 2023", "2023-12-31".formatReleaseDate())
        assertEquals("Jul 4, 2000", "2000-07-04".formatReleaseDate())
    }

    @Test
    fun formatReleaseDate_blankString_returnsEmpty() {
        assertEquals("", "".formatReleaseDate())
        assertEquals("", "   ".formatReleaseDate())
    }

    @Test
    fun formatReleaseDate_invalidDate_returnsOriginalString() {
        assertEquals("not-a-date", "not-a-date".formatReleaseDate())
        assertEquals("2024-13-01", "2024-13-01".formatReleaseDate())
        assertEquals("invalid", "invalid".formatReleaseDate())
    }
    // endregion

    // region formatRuntime
    @Test
    fun formatRuntime_onlyMinutes_returnsMinutesOnly() {
        assertEquals("0m", 0.formatRuntime())
        assertEquals("45m", 45.formatRuntime())
        assertEquals("59m", 59.formatRuntime())
    }

    @Test
    fun formatRuntime_hoursAndMinutes_returnsBoth() {
        assertEquals("1h 0m", 60.formatRuntime())
        assertEquals("1h 30m", 90.formatRuntime())
        assertEquals("2h 5m", 125.formatRuntime())
        assertEquals("3h 45m", 225.formatRuntime())
    }

    @Test
    fun formatRuntime_exactHours_returnsHoursWithZeroMinutes() {
        assertEquals("2h 0m", 120.formatRuntime())
    }
    // endregion
}
