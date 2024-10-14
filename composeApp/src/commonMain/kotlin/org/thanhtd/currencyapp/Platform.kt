package org.thanhtd.currencyapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform