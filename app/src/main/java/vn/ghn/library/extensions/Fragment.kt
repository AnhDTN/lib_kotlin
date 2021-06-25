package vn.ghn.library.extensions

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.fragment.app.*
import vn.ghn.library.R

val HORIZONTAL_ANIMATIONS = intArrayOf(
    R.anim.horizontal_enter,
    R.anim.horizontal_exit,
    R.anim.horizontal_pop_enter,
    R.anim.horizontal_pop_exit
)

val VERTICAL_ANIMATIONS = intArrayOf(
    R.anim.vertical_enter,
    R.anim.vertical_exit,
    R.anim.vertical_pop_enter,
    R.anim.vertical_pop_exit
)

val PARALLAX_ANIMATIONS = intArrayOf(
    R.anim.parallax_enter,
    R.anim.parallax_exit,
    R.anim.parallax_pop_enter,
    R.anim.parallax_pop_exit
)


/**
 * Fragment extensions
* */

fun Fragment.addFragment(
    fragment: Fragment,
    @IdRes container: Int,
    backStack: Boolean = true,
    animations: IntArray? = VERTICAL_ANIMATIONS
) {
    val tag = fragment::class.java.simpleName
    childFragmentManager.scheduleTransaction({
        add(container, fragment, tag)
        if (backStack) {
            addToBackStack(tag)
        }
    }, animations)
}

fun Fragment.replaceFragment(
    fragment: Fragment,
    @IdRes container: Int,
    backStack: Boolean = true,
    animations: IntArray? = VERTICAL_ANIMATIONS
) {
    val tag = fragment::class.java.simpleName
    childFragmentManager.scheduleTransaction({
        replace(container, fragment, tag)
        if (backStack) {
            addToBackStack(tag)
        }
    }, animations)
}


fun Fragment.removeFragment(tag: String?, animations: IntArray? = VERTICAL_ANIMATIONS) {
    tag ?: return
    val fragment: Fragment = childFragmentManager.findFragmentByTag(tag) ?: return
    childFragmentManager.scheduleTransaction({
        remove(fragment)
    }, animations)
}

fun Fragment.removeFragment(cls: Class<Fragment>, animations: IntArray? = VERTICAL_ANIMATIONS) {
    removeFragment(cls.simpleName, animations)
}


fun Fragment.isNotExist(cls: Class<*>): Boolean {
    val tag = cls.simpleName
    return null == childFragmentManager.findFragmentByTag(tag)
}


fun Fragment.clearStack() {
    val sfm = childFragmentManager
    for(fragment in sfm.fragments) {
        if(fragment !is Fragment) continue
        sfm.beginTransaction().remove(fragment).commit()
    }
    sfm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun Fragment?.hideKeyboard() {
    this?.requireActivity()?.hideKeyboard()
}

fun Fragment.showKeyboard() {
    this.requireActivity().showKeyboard()
}
/**
 * FragmentActivity extensions
* */


fun FragmentActivity.addFragment(
    fragment: Fragment,
    @IdRes container: Int,
    backStack: Boolean = true,
    animations: IntArray? = VERTICAL_ANIMATIONS
) {
    val tag = fragment::class.java.simpleName
    supportFragmentManager.scheduleTransaction({
        add(container, fragment, tag)
        if (backStack) {
            addToBackStack(tag)
        }
    }, animations)
}

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    @IdRes container: Int,
    backStack: Boolean = true,
    animations: IntArray? = VERTICAL_ANIMATIONS
) {
    val tag = fragment::class.java.simpleName
    supportFragmentManager.scheduleTransaction({
        replace(container, fragment, tag)
        if (backStack) {
            addToBackStack(tag)
        }
    }, animations)
}


fun FragmentActivity.removeFragment(tag: String?, animations: IntArray? = VERTICAL_ANIMATIONS) {
    tag ?: return
    val fragment: Fragment = supportFragmentManager.findFragmentByTag(tag) ?: return
    supportFragmentManager.scheduleTransaction({
        remove(fragment)
    }, animations)
}

fun FragmentActivity.removeFragment(cls: Class<*>, animations: IntArray? = VERTICAL_ANIMATIONS) {
    removeFragment(cls.simpleName, animations)
}

fun FragmentActivity.clearStack() {
    val sfm = supportFragmentManager
    for(fragment in sfm.fragments) {
        if(fragment !is Fragment) continue
        sfm.beginTransaction().remove(fragment).commit()
    }
    sfm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun FragmentActivity.isNotExist(cls: Class<*>): Boolean {
    val tag = cls.simpleName
    return null == supportFragmentManager.findFragmentByTag(tag)
}

/**
 *  FragmentManager extensions
* */

fun FragmentManager.scheduleTransaction(
    block: FragmentTransaction.() -> Unit,
    animations: IntArray? = VERTICAL_ANIMATIONS
) {

    val transaction = beginTransaction()
    if (null != animations) transaction.setCustomAnimations(
        animations[0],
        animations[1],
        animations[2],
        animations[3]
    )
    transaction.block()
    transaction.commitAllowingStateLoss()

}

fun DialogFragment?.hideSystemUI() {
    this?.dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    this?.hideKeyboard()
    hideStatusBar()
    hideNavigationBar()
}


fun DialogFragment?.hideStatusBar() {
    this ?: return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

        dialog?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        @Suppress("DEPRECATION")
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

@SuppressLint("ObsoleteSdkInt")
fun DialogFragment?.hideNavigationBar(hasFocus: Boolean = true) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && hasFocus) this?.dialog?.window?.apply {
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
        val decorView = this?.dialog?.window?.decorView ?: return
        decorView.systemUiVisibility = flags
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                decorView.systemUiVisibility = flags
            }
        }
    }
}