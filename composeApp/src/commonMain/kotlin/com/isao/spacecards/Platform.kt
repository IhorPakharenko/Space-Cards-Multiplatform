package com.isao.spacecards

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform