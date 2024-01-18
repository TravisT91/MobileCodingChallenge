package com.hopskipdrive.mobileCodingChallenge.myrides.data.networking

sealed class  Response<T : Any?> {
    data class Success<T>(val value: T) : Response<T>()
    data class Error<T>(val message: String?) : Response<T>()
}