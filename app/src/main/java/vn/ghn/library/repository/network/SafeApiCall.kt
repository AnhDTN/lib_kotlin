package vn.ghn.library.repository.network

import android.accounts.NetworkErrorException
import android.net.ParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ApiDataSource<T> {
        return withContext(Dispatchers.IO) {
            try {
                ApiDataSource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        ApiDataSource.Failure(
                            true,
                            throwable.code(),
                            throwable.response()?.errorBody(),
                            ""
                        )
                    }
                    is NetworkErrorException, is SocketTimeoutException, is TimeoutException -> {
                        ApiDataSource.Failure(false, null, null, "Network error")
                    }
                    is ParseException -> {
                        ApiDataSource.Failure(false, 998, null, "Parsing data failure")
                    }
                    else -> ApiDataSource.Failure(false, 999, null, "Something went wrong")
                }
            }
        }
    }
}