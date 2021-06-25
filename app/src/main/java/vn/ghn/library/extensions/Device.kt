package vn.ghn.library.extensions

import android.app.Application
import android.os.Build
import android.provider.Settings
import vn.ghn.library.Library
import java.util.*
import java.util.concurrent.TimeUnit

private val app: Application get() = Library.app

val deviceId : String get() = Settings.Secure.getString(app.contentResolver, Settings.Secure.ANDROID_ID)

val osVersion: String
    get() = Build.VERSION.RELEASE

val osVersionCode: Int
    get() = Build.VERSION.SDK_INT

val deviceModel: String
    get() {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            Build.MODEL.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            Build.MODEL
        }
    }

val deviceName : String
    get() {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            Build.MODEL.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } else {
            Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + " " + Build.MODEL
        }
    }

val timeZone: String get() {
    val cal = GregorianCalendar()
    val timeZone = cal.timeZone
    val mGMTOffset = timeZone.rawOffset
    return "GMT+" + TimeUnit.HOURS.convert(mGMTOffset.toLong(), TimeUnit.MILLISECONDS)
}