package vn.ghn.library.view.adapter.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


/**
 *      https://medium.com/codex/33-viewpager2-transformers-for-your-android-uis-bbdab801eb2b
* */


class PageTransformer {
    inner class ParallaxPageTransformer(val parallaxViewId: () -> Int) : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            val pageWidth: Int = view.width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.alpha = 1f
                }
                position <= 1 -> { // [-1,1]
                    view.findViewById<View>(parallaxViewId())?.translationX = -position * (view.width / 2)                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.alpha = 1f
                }
            }
        }
    }


    class FadeZoom : ViewPager2.PageTransformer {

        companion object {
            private const val MIN_SCALE = 0.85f
            private const val MIN_ALPHA = 0.5f
        }

        override fun transformPage(view: View, position: Float) {

            val pageWidth = view.width
            val pageHeight = view.height

            when {

                // [-Infinity,-1) This page is way off-screen to the left.
                position < -1 -> view.alpha = 0f

                // Modify the default slide transition to shrink the page as well
                position <= 1 -> { // [-1,1]
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                    val horizontalMargin = pageWidth * (1 - scaleFactor) / 2
                    if (position < 0) {
                        view.translationX = horizontalMargin - verticalMargin / 2
                    } else {
                        view.translationX = -horizontalMargin + verticalMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.scaleX = scaleFactor
                    view.scaleY = scaleFactor

                    // Fade the page relative to its size.
                    view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)

                }
                // (1,+Infinity] This page is way off-screen to the right.
                else -> view.alpha = 0f
            }
        }
    }

    class Stack : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            val pageWidth = view.width

            val pageHeight = view.height

            if (-1 < position && position < 0) {

                val scaleFactor = 1 - Math.abs(position) * 0.1f
                val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                val horizontalMargin = pageWidth * (1 - scaleFactor) / 2

                if (position < 0) {
                    view.translationX = horizontalMargin - verticalMargin / 2
                } else {
                    view.translationX = -horizontalMargin + verticalMargin / 2
                }

                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            }

        }
    }

    class VerticalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            when {
                position < -1 -> {
                    view.alpha = 0f
                }
                position <= 1 -> {
                    view.alpha = 1f
                    view.translationX = view.width * -position
                    val yPosition = position * view.height
                    view.translationY = yPosition
                }
                else -> {
                    view.alpha = 0f
                }
            }
        }
    }

    class ReservedVerticalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            }

        }
    }

    class HorizontalSlide : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {

            view.translationX = view.width * -position

            if (position > 0) {
                view.translationY = position * view.height
            } else {
                view.translationY = position * -view.height
            }

        }
    }

    class Fade : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (position <= -1.0F || position >= 1.0F) {
                page.alpha = 0.0F
            } else if (position == 0.0F) {
                page.alpha = 1.0F
            } else {
                page.alpha = 1.0F - abs(position)
            }
        }
    }

    class None : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            when {
                position < 0 -> view.scrollX = (view.width.toFloat() * position).toInt()
                position > 0 -> view.scrollX = (-(view.width.toFloat() * -position)).toInt()
                else -> view.scrollX = 0
            }
        }
    }

    class Gate : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.translationX = -position * page.width
            when {
                position < -1 -> {    // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.pivotX = 0f
                    page.rotationY = 90 * abs(position)
                }
                position <= 1 -> {    // (0,1]
                    page.alpha = 1f
                    page.pivotX = page.width.toFloat()
                    page.rotationY = -90 * abs(position)
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
    }


    class Fidget : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.translationX = -position * page.width
            if (abs(position) < 0.5) {
                page.visibility = View.VISIBLE
                page.scaleX = 1 - abs(position)
                page.scaleY = 1 - abs(position)
            } else if (abs(position) > 0.5) {
                page.visibility = View.GONE
            }
            when {
                position < -1 -> {     // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.rotation =
                        36000 * (abs(position) * abs(position) * abs(position) * abs(
                            position
                        ) * abs(position) * abs(position) * abs(position))
                }
                position <= 1 -> {    // (0,1]
                    page.alpha = 1f
                    page.rotation =
                        -36000 * (abs(position) * abs(position) * abs(position) * abs(
                            position
                        ) * abs(position) * abs(position) * abs(position))
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
    }

    class Pop : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.translationX = -position * page.width
            if (abs(position) < 0.5) {
                page.visibility = View.VISIBLE
                page.scaleX = 1 - abs(position)
                page.scaleY = 1 - abs(position)
            } else if (abs(position) > 0.5) {
                page.visibility = View.GONE
            }
        }
    }

    class Accordion : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.pivotX = if (position < 0.0f) 0.0f else page.width.toFloat()
            page.scaleX = if (position < 0.0f) 1.0f + position else 1.0f - position
        }
    }


    class Depth : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> {    // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.translationX = 0f
                    page.scaleX = 1f
                    page.scaleY = 1f
                }
                position <= 1 -> {    // (0,1]
                    page.translationX = -position * page.width
                    page.alpha = 1 - abs(position)
                    page.scaleX = 1 - abs(position)
                    page.scaleY = 1 - abs(position)
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
    }

    class ZoomIn : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val scale = if (position < 0.0f) position + 1.0f else abs(1.0f - position)
            page.scaleX = scale
            page.scaleY = scale
            page.pivotX = page.width.toFloat() * 0.5f
            page.pivotY = page.height.toFloat() * 0.5f
            page.alpha = if (position >= -1.0f && position <= 1.0f) 1.0f - (scale - 1.0f) else 0.0f
        }
    }

    class ZoomOutSlide : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (position >= -1.0f || position <= 1.0f) {
                val height = page.height.toFloat()
                val scaleFactor = 0.85f.coerceAtLeast(1.0f - abs(position))
                val vertMargin = height * (1.0f - scaleFactor) / 2.0f
                val horzMargin = page.width.toFloat() * (1.0f - scaleFactor) / 2.0f
                page.pivotY = 0.5f * height
                if (position < 0.0f) {
                    page.translationX = horzMargin - vertMargin / 2.0f
                } else {
                    page.translationX = -horzMargin + vertMargin / 2.0f
                }
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.alpha = 0.5f + (scaleFactor - 0.85f) / 0.14999998f * 0.5f
            }
        }
    }

    class ZoomOut : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> {  // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    page.scaleX = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    page.scaleY = MIN_SCALE.coerceAtLeast(1 - abs(position))
                    page.alpha = MIN_ALPHA.coerceAtLeast(1 - abs(position))
                }
                else -> {  // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }

        companion object {
            private const val MIN_SCALE = 0.65f
            private const val MIN_ALPHA = 0.3f
        }
    }

    class CubeOutScaling: ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> {    // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.pivotX = page.width.toFloat()
                    page.rotationY = -90 * Math.abs(position)
                }
                position <= 1 -> {    // (0,1]
                    page.alpha = 1f
                    page.pivotX = 0f
                    page.rotationY = 90 * Math.abs(position)
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
            if (abs(position) <= 0.5) {
                page.scaleY = 0.4f.coerceAtLeast(1 - abs(position))
            } else if (abs(position) <= 1) {
                page.scaleY = 0.4f.coerceAtLeast(abs(position))
            }
        }
    }

    class CubeIn : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            page.cameraDistance = 20000f
            when {
                position < -1 -> {     // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0f
                }
                position <= 0 -> {    // [-1,0]
                    page.alpha = 1f
                    page.pivotX = page.width.toFloat()
                    page.rotationY = 90 * abs(position)
                }
                position <= 1 -> {    // (0,1]
                    page.alpha = 1f
                    page.pivotX = 0f
                    page.rotationY = -90 * abs(position)
                }
                else -> {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0f
                }
            }
        }
    }
}