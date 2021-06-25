package vn.ghn.library.extensions

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 *  Activity extensions keyboard
 * */

fun Activity.showKeyboard() {
//    if (currentFocus?.windowToken != null) {
//        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED, 0)
//    }
    window.insetsController?.show(WindowInsets.Type.ime())

}

fun Activity?.hideKeyboard() {
    this ?: return
//    val view = this.currentFocus
//    if (view != null) {
//        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm?.hideSoftInputFromWindow(view.windowToken, 0)
//    }
    window.insetsController?.hide(WindowInsets.Type.ime())
}


fun Activity.setPositionKeyboard() {
    val imeInsets = currentFocus?.rootWindowInsets?.getInsets(WindowInsets.Type.ime()) ?: return
    currentFocus?.translationX = imeInsets.bottom.toFloat()
}

fun Activity?.lightStatusBar() {
    this ?: return
    window.insetsController?.show(8192)
}

fun Activity?.darkStatusBar() {
    this ?: return
    window.insetsController?.show(0)
}

fun Activity?.statusBarDrawable(drawable: Drawable?) {
    this ?: return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (drawable is ColorDrawable) {
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.statusBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(drawable)
    }
}

fun Activity?.navigationBarDrawable(drawable: Drawable?) {
    this ?: return
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (drawable is ColorDrawable) {
        window.navigationBarColor = drawable.color
    } else {
        window.navigationBarColor = Color.TRANSPARENT
        window.setBackgroundDrawable(drawable)
    }
}

fun Activity?.navigationBarColorRes(@ColorRes res: Int) {
    this ?: return
    navigationBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity?.navigationBarDrawable(@DrawableRes res: Int) {
    this ?: return
    navigationBarDrawable(ContextCompat.getDrawable(this, res))
}

fun Activity?.hideNavigationBar(hasFocus: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hasFocus) this?.window?.apply {
        setDecorFitsSystemWindows(false)
        return
    }
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val decorView = this?.window?.decorView ?: return
        decorView.systemUiVisibility = flags
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
    }
}