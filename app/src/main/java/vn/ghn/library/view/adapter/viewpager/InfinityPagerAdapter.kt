package vn.ghn.library.view.adapter.viewpager

abstract class InfinityPagerAdapter<T> : ViewPagerAdapter<T>() {
    override fun getCount(): Int {
        return data.size * 1000
    }

    override fun get(position: Int): T? {
        if (data.isEmpty()) return null
        return data[position % size]
    }
}