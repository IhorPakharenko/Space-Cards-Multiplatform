package com.isao.yfoo3.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.isao.yfoo3.resources.Res
import com.isao.yfoo3.resources.feed
import com.isao.yfoo3.resources.liked
import org.jetbrains.compose.resources.StringResource

enum class BottomNavigationScreen(
    val route: String,
    val nameRes: StringResource,
    val icon: ImageVector
) {
    Feed(Screen.Feed.route, Res.string.feed, Icons.Filled.Explore),
    Liked(Screen.Liked.route, Res.string.liked, Icons.Filled.Favorite),
}