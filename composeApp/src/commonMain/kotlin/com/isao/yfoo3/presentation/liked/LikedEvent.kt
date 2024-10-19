package com.isao.yfoo3.presentation.liked

sealed class LikedEvent {

    data class OpenWebBrowser(val uri: String) : LikedEvent()
}