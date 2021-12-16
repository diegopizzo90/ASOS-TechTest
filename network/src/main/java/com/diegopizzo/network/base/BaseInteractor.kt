package com.diegopizzo.network.base

import retrofit2.Response

open class BaseInteractor {
    fun <T> handleResponse(response: Response<T>?): Result<T> {
        return if (response?.isSuccessful.isTrue() && response?.body() != null) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(response?.code())
        }
    }
}

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val errorCode: Int? = null) : Result<T>()
}