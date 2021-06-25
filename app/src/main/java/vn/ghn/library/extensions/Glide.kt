package vn.ghn.library.extensions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

@GlideModule
class MyGlideApp : AppGlideModule()

interface SimpleRequestListener : RequestListener<Drawable> {
    fun onComplete()
    fun onFailure()
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        onFailure()
        return true
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        onComplete()
        return true
    }
}

fun ImageView.load(url: String?, block: RequestOptions) {
    Glide
        .with(context)
        .load(url)
        .apply(block)
        .into(this)
}


fun ImageView.load(bitmap: Bitmap?, block: RequestOptions) {
    Glide
        .with(context)
        .load(bitmap)
        .apply(block)
        .into(this)
}

fun ImageView.load(res: Int, block: RequestOptions) {
    Glide
        .with(context)
        .load(res)
        .apply(block)
        .into(this)
}

fun ImageView.load(bytes: ByteArray?, block: RequestOptions) {
    Glide
        .with(context)
        .load(bytes)
        .apply(block)
        .into(this)
}

fun ImageView.load(drawable: Drawable?, block: RequestOptions) {
    Glide
        .with(context)
        .load(drawable)
        .apply(block)
        .into(this)
}
