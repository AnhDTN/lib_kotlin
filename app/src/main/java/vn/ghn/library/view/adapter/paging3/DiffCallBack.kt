package vn.ghn.library.view.adapter.paging3

import androidx.recyclerview.widget.DiffUtil

open class DiffCallBack<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = false

    override fun areContentsTheSame(oldItem: T, newItem: T) = false
}