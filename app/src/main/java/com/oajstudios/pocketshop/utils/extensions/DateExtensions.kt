package com.oajstudios.pocketshop.utils.extensions

import com.oajstudios.pocketshop.utils.Constants

fun toDate(string: String, currentFormat: Int = Constants.DateFormatCodes.YMD_HMS): String {
    return when (currentFormat) {
        Constants.DateFormatCodes.YMD_HMS -> Constants.DD_MMM_YYYY.format(Constants.FULL_DATE_FORMATTER.parse(string)!!)
        Constants.DateFormatCodes.YMD -> Constants.DD_MMM_YYYY.format(Constants.YYYY_MM_DD.parse(string)!!)
        else -> string
    }
}