package com.isao.yfoo3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform