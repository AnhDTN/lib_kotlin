package vn.ghn.library.extensions

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.StringRes
import vn.ghn.library.Library

private val app: Application get() = Library.app

private val versionApp: String
    get() {
        return try {
            app.packageManager.getPackageInfo(app.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return "v1.0"
        }
    }

val packageName: String get() = app.applicationContext.packageName

val packageUrl: String get() = "package:$packageName"

fun toast(message: String?) {
    message ?: return
    if (isOnUiThread) {
        Toast.makeText(app.applicationContext, message, Toast.LENGTH_SHORT).show()
    } else uiHandler.post {
        Toast.makeText(app.applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}


fun toast(@StringRes res: Int?, vararg arguments: Any) {
    res ?: return
    val message = try {
        app.resources.getString(res, *arguments)
    } catch (ex: Resources.NotFoundException) {
        return
    }
    toast(message)
}

fun restartApp() {
    val intent = app.packageManager.getLaunchIntentForPackage(packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    app.startActivity(intent)
}