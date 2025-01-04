package com.isao.spacecards.core.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.isao.spacecards.resources.Res
import com.isao.spacecards.resources.feed
import com.isao.spacecards.resources.liked
import org.jetbrains.compose.resources.StringResource

enum class TopLevelScreen(
  val route: String,
  val nameRes: StringResource,
  val icon: ImageVector,
) {
  Feed(Screen.Feed.route, Res.string.feed, Icons.Filled.Explore),
  Liked(Screen.Liked.route, Res.string.liked, Icons.Filled.Favorite),
}
