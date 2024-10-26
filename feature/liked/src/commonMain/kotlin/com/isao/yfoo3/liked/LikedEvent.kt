package com.isao.yfoo3.liked

sealed class LikedEvent {

    data class OpenWebBrowser(val uri: String) : LikedEvent()
}