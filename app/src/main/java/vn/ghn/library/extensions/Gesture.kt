package vn.ghn.library.extensions

import android.view.View
import vn.ghn.library.view.base.ViewClickListener

fun View.addViewClickListener(block: (View?) -> Unit) {
    setOnClickListener(object : ViewClickListener() {
        override fun onClicks(v: View?) {
            block(v)
        }
    })
}