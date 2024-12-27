package com.isao.spacecards.liked

sealed class LikedEvent {
  data class OpenWebBrowser(val uri: String) : LikedEvent()
}
