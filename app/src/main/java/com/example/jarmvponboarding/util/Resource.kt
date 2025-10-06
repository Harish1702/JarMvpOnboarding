package com.example.jarmvponboarding.util

// A generic wrapper class for handling states like Success, Error, Loading.
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
}
