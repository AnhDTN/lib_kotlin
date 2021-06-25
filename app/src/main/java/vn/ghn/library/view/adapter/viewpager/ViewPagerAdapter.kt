package vn.ghn.library.view.adapter.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class ViewPagerAdapter<T> : PagerAdapter() {
    var selectedItem: T? = null

    var data: MutableList<T> = mutableListOf()

    val size: Int get() = data.size

    abstract fun resId(): Int
    abstract fun View.onBind(model: T);
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(resId(), container, false)
        view.onBind(data[position])
        container.addView(view, position)
        return view
    }

    override fun getCount(): Int = size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    open fun add(element: T) {
        element ?: return
        data.add(element)
        notifyDataSetChanged()
    }

    open fun addAll(collection: List<T>?) {
        collection ?: return
        data.addAll(collection)
        notifyDataSetChanged()
    }

    open fun get(position: Int): T? {
        if (data.isEmpty())
            return null
        if (position !in 0 until data.size)
            return null
        return data[position]
    }

    open fun set(element: T, position: Int) {
        element ?: return
        data[position] = element
        notifyDataSetChanged()
    }

    open fun indexOf(element: T): Int {
        element ?: return -1
        return data.indexOf(element)
    }

    open fun clear() {
        data = mutableListOf()
        notifyDataSetChanged()
    }

    open fun selectedPosition(): Int {
        selectedItem ?: return -1
        return indexOf(selectedItem!!)
    }
}