package org.top.test

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform