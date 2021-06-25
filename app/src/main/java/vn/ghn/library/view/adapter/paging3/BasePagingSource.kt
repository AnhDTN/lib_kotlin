package vn.ghn.library.view.adapter.paging3
import androidx.paging.PagingSource
import androidx.paging.PagingState
abstract class BasePagingSource<V : Any> :
    PagingSource<Int, V>() {
    private var START_PAGE = 1
    abstract val block: (page: Int) -> List<V>

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        return try {
            LoadResult.Page(
                data = block(params.key ?: START_PAGE),
                prevKey = null,
                nextKey = if (params.key == null) START_PAGE + 1 else params.key!! + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}