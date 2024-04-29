package cn.codebro.j_anime_android.net

import android.util.Log
import cn.codebro.j_anime_android.core.IView
import cn.codebro.j_anime_android.pojo.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<T>(open val view: IView) : Callback<ApiResponse<T>> {

    override fun onResponse(call: Call<ApiResponse<T>>, response: Response<ApiResponse<T>>) {
        val body = response.body()
        if (body == null) {
            onFailure(
                call,
                IllegalArgumentException("The HTTP response body is empty. Please check out your response.")
            )
            return
        }
        when (body.code) {
            200 -> {
                onResponseSuccess(body)
            }

            4001 -> {
                view.notLogin()
            }

            500 -> {
                if (body.message == null) view.showToast("网络异常~")
                else view.showToast(body.message!!)
            }

            else -> {
                view.showToast("未定义响应码")
            }
        }
    }

    abstract fun onResponseSuccess(response: ApiResponse<T>)

    override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
        view.showToast("网络异常，请检查网络连接！")
    }

}