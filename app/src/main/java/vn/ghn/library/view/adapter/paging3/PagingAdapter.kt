package vn.ghn.library.view.adapter.paging3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*
import vn.ghn.library.view.adapter.recycler.ViewHolder
import vn.ghn.library.extensions.addViewClickListener


abstract class PagingAdapter<T : Any>(
    private val diffCallBack: DiffCallBack<T>,
    adapter: PagingAdapter<T>
) :
    PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallBack) {


    private val mDiffer: AsyncPagingDataDiffer<T> =
        asyncPagedListDiffer(diffCallBack)

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
        val model = mDiffer.peek(position) ?: return
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
            return footerLayoutResource
        }
        val model = mDiffer.peek(position) ?: return 0
        return layoutResource(model, position)
    }

    private fun asyncPagedListDiffer(itemCallback: DiffUtil.ItemCallback<T>): AsyncPagingDataDiffer<T> {

        val adapterCallback = AdapterListUpdateCallback(this)
        val listCallback = object : ListUpdateCallback {
            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
            }

            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count + 1)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count)
            }
        }
        return AsyncPagingDataDiffer<T>(itemCallback, listCallback)
    }

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