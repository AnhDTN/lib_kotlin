package vn.ghn.library.view.adapter.recycler

import androidx.recyclerview.widget.DiffUtil

open class DiffUtilsCallBack<T> (private val oldData : MutableList<T>, private val newData: MutableList<T>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return false
    }

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return false
    }

}