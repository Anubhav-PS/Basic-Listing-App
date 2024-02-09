package com.anubhav.swipetask.services.retrofit

import retrofit2.Response

data class ServerResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {

    companion object {

        fun <T> success(data: Response<T>): ServerResponse<T> {
            return ServerResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception): ServerResponse<T> {
            return ServerResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }
    }

    sealed class Status {
        object Success : Status()
        object Failure : Status()
    }

    val failed: Boolean
        get() = this.status == Status.Failure
    val isSuccessful: Boolean
        get() = !failed && this.data?.isSuccessful == true
    val body: T
        get() = this.data!!.body()!!

}