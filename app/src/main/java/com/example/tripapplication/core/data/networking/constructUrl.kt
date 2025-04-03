package com.example.tripapplication.core.data.networking

import com.example.tripapplication.BuildConfig.BASE_URL


//to create the url for different end points

fun constructUrl(url : String) : String {
    return when {
        url.contains(BASE_URL) -> url
        url.startsWith("/") -> BASE_URL + url.drop(1)
        else -> BASE_URL + url

    }
}