package vn.ghn.library.view.adapter.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.ghn.library.extensions.addViewClickListener

class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

abstract class RecyclerAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     *     Data
     */

    var data: MutableList<T> = mutableListOf()

    /**
     * Layout resource for empty data.
     */
    @LayoutRes
    open var blankLayoutResource = 0


    /**
     * Layout resource for footer data.
     */
    @LayoutRes
    open var footerLayoutResource = 0


    /**
     * Item view click
     */
    var onItemClick: (T, Int) -> Unit = { _, _ -> }

    var onItemLongClick: (T, Int) -> Unit = { _, _ -> }

    val size: Int get() = data.size

    @LayoutRes
    protected abstract fun layoutResource(model: T, position: Int): Int
    protected abstract fun View.onBindModel(model: T, position: Int, @LayoutRes layout: Int)

    open fun onCreateItemView(parent: ViewGroup, viewType: Int): View {
        return if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = if (viewType == 0) {
            View(parent.context).apply { visibility = View.GONE }
        } else {
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        }
        return ViewHolder(v)
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        when (type) {
            0, blankLayoutResource, footerLayoutResource -> return
        }
        val model = get(position) ?: return
        vh.itemView.apply {
            onBindModel(model, position, type)
            addViewClickListener {
                onItemClick(model, position)
            }
            setOnLongClickListener {
                onItemLongClick(model, position)
                true
            }
        }
    }

    override fun getItemCount(): Int =
        if (blankLayoutResource != 0 || footerLayoutResource != 0) size + 1
        else size

    override fun getItemViewType(position: Int): Int {
        if (data.isNullOrEmpty() && blankLayoutResource != 0) {
            return blankLayoutResource
        }
        if (data.isNullOrEmpty() && footerLayoutResource != 0 && position == size) {
            if (position > data.lastIndex) onFooterIndexChanged(position)
            return footerLayoutResource
        }
        val model = get(position) ?: return 0
        return layoutResource(model, position)
    }


    private fun set(collection: Collection<T>?) {
        data = collection?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    private fun set(collection: ArrayList<T>?) {
        data = collection?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    private fun set(collection: MutableList<T>?) {
        data = collection ?: mutableListOf()
        notifyDataSetChanged()
    }

    private fun set(collection: Array<T>?) {
        data = collection?.toMutableList() ?: mutableListOf()
        notifyDataSetChanged()
    }

    open fun setIfNotNullOrEmpty(arrayList: ArrayList<T>?) {
        if (arrayList.isNullOrEmpty()) return
        set(arrayList)
    }

    open fun setIfNotNullOrEmpty(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        set(collection)
    }

    open fun setIfNotNullOrEmpty(list: MutableList<T>?) {
        if (list.isNullOrEmpty()) return
        set(list)
    }

    open fun setIfNotNullOrEmpty(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        set(array)
    }

    private fun add(arrayList: ArrayList<T>?) {
        if (arrayList.isNullOrEmpty()) return
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilsCallBack<T>(
                    oldData = data,
                    newData = arrayList
                )
            )
        this.data.clear()
        data.addAll(arrayList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun add(mutableList: MutableList<T>?) {
        if (mutableList.isNullOrEmpty()) return
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilsCallBack<T>(
                    oldData = data,
                    newData = mutableList
                )
            )
        this.data.clear()
        data.addAll(mutableList)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun add(collection: Collection<T>?) {
        if (collection.isNullOrEmpty()) return
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilsCallBack<T>(
                    oldData = data,
                    newData = collection.toMutableList()
                )
            )
        this.data.clear()
        data.addAll(collection)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun add(array: Array<T>?) {
        if (array.isNullOrEmpty()) return
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilsCallBack<T>(
                    oldData = data,
                    newData = array.toMutableList()
                )
            )
        this.data.clear()
        data.addAll(array)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun add(element: T?) {
        element ?: return
        data.add(element)
        notifyItemChanged(size, data.size + 1)
    }

    private fun add(element: T?, index: Int) {
        element ?: return
        val newData = mutableListOf<T>()
        newData.addAll(data)
        newData.add(index, element)
        val diffResult: DiffUtil.DiffResult =
            DiffUtil.calculateDiff(
                DiffUtilsCallBack<T>(
                    oldData = data,
                    newData = newData
                )
            )
        this.data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    open fun edit(position: Int, model: T?) {
        model ?: return
        if (position in 0..size) {
            data[position] = model
            notifyItemChanged(position)
        }
    }

    open fun remove(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun remove(model: T?) {
        model ?: return
        val position = data.indexOf(model)
        remove(position)
    }

    open fun clear() {
        data = mutableListOf()
        notifyDataSetChanged()
    }

    open fun get(position: Int): T? {
        if (position in 0..data.lastIndex) return data[position]
        return null
    }


    fun showFooter(@LayoutRes res: Int) {
        footerLayoutResource = res
        notifyItemChanged(size)
    }

    fun hideFooter() {
        footerLayoutResource = 0
        notifyItemChanged(size)
    }

    var onFooterIndexChanged: (Int) -> Unit = {}

    open fun bind(recyclerView: RecyclerView, block: LinearLayoutManager.() -> Unit = {}) {
        val lm = LinearLayoutManager(recyclerView.context)
        lm.block()
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }

    open fun bind(
        recyclerView: RecyclerView,
        spanCount: Int,
        block: GridLayoutManager.() -> Unit = {}
    ) {
        val lm = GridLayoutManager(recyclerView.context, spanCount)
        lm.block()
        lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (data.isNullOrEmpty() || position == size) lm.spanCount
                else 1
            }
        }
        recyclerView.layoutManager = lm
        recyclerView.adapter = this
    }
}