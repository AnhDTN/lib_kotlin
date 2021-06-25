package vn.ghn.library.extensions

import android.util.Patterns
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.SimpleDateFormat
import java.util.*


/**
 * parse list to array
 */
fun <T> parse(list: List<T>): ArrayList<T> {
    val array = arrayListOf<T>()
    array.addAll(list)
    return array
}

/**
 * parse json
 */

fun <T> parse(s: String, cls: Class<T>): T? {
    if (s.isEmpty()) {
        return null
    }
    return try {
        val gson = Gson()
        gson.fromJson(s, cls)
    } catch (e: IllegalStateException) {
        null
    } catch (e: JsonSyntaxException) {
        null
    } catch (e: IllegalArgumentException) {
        null
    }
}


/**
 * String extentions
 * */


fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPhone(): Boolean {
    return Patterns.PHONE.matcher(this).matches()
}


fun String.parseUTCtoLocalTime(): String {
    val df = SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH)
    df.timeZone = TimeZone.getTimeZone("UTC")
    val date = df.parse(this)
    df.timeZone = TimeZone.getDefault()
    return df.format(date!!)
}
