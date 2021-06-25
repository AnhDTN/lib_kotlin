package vn.ghn.library.view.base

import android.os.SystemClock
import android.view.View

abstract class ViewClickListener : View.OnClickListener{
    private var lastClickTime: Long = 0

    private var lastClickViewId: Int = -1

    abstract fun onClicks(v: View?)

    final override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClickTime > 500 || v?.id != lastClickViewId) {
            lastClickViewId = v?.id ?: -1
            onClicks(v)
        }

        lastClickTime = SystemClock.elapsedRealtime()
    }

}