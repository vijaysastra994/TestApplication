package com.vijay.albums.utils

interface Mapper<T, U> {

    fun map(input:T):U
}
