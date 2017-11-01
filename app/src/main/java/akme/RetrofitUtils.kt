package akme

import retrofit2.Call

fun <T> Call<T>.toService(): Service<T>  {
    val response= this.execute()

    return if (response.isSuccessful) {
        ServiceRight { response.body() }
    } else {
        ServiceLeft<T>(AkmeException.ApiException(""))
    }
}