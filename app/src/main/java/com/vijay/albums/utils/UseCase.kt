package com.vijay.albums.utils

interface UseCase<INPUT, OUTPUT> {

    fun run(input: INPUT): OUTPUT
}
