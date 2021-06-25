package vn.ghn.library.repository.network

import okhttp3.ResponseBody

sealed class ApiDataSource<out T> {
    data class Success<out T>(val value: T) : ApiDataSource<T>()
    data class Failure(
        val isHttpException: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val message: String
    ) : ApiDataSource<Nothing>()
    object Loading : ApiDataSource<Nothing>()
}