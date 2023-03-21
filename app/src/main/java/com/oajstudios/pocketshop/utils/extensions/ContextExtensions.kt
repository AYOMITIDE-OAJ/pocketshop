package com.oajstudios.pocketshop.utils.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat

fun Context.isGPSEnable(): Boolean = getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)


fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


inline fun <reified T : Any> Context.launchActivity(options: Bundle? = null, noinline init: Intent.() -> Unit = {}) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

/**
 * Check if an App is Installed on the user device.
 */

@Throws(PackageManager.NameNotFoundException::class)
fun Context.getAppVersionName(pName: String = packageName): String = packageManager.getPackageInfo(pName, 0).versionName


fun Context.dialNumber(number: String) = startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))

fun Context.isPermissionGranted(permissions: Array<String>): Boolean {
    permissions.forEach {
        if (ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)
            return false
    }
    return true
}