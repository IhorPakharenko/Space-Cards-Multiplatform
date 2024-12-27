@file:OptIn(ExperimentalFoundationApi::class)

package com.isao.yfoo3.core.designsystem.composable.dismissible

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.unit.round

@Composable
fun <Item> DismissibleStack(
  items: List<Item>,
  modifier: Modifier = Modifier,
  transformTopItem: @Composable ((Item) -> Unit)? = null,
  // We want content state to be reset
  content: @Composable (Item) -> Unit,
) {
  val movableContent = remember { movableContentOf(content) }

  var index by remember(items) { mutableIntStateOf(0) }
  var offset by remember(index) { mutableStateOf(Offset.Zero) }
  val draggableState = rememberDraggable2DState {
    offset += it
  }

  val topItem = items.getOrNull(index)
  val bottomItem = items.getOrNull(index + 1)
  val bottomerItem = items.getOrNull(index + 2)

  var startAnchor by remember { mutableIntStateOf(0) }
  var endAnchor by remember { mutableIntStateOf(0) }
  var topAnchor by remember { mutableIntStateOf(0) }
  var bottomAnchor by remember { mutableIntStateOf(0) }

  Box(
    modifier
      .draggable2D(
        draggableState,
        onDragStopped = { velocity ->
          calculateClosestAnchor(offset, startAnchor, endAnchor, topAnchor, bottomAnchor)
          index++
        },
      ).onPlaced {
        // Calculate the values of startAnchor and endAnchor based on the parents bounds
        startAnchor = -it.size.width / 2
        endAnchor = it.size.width / 2
        topAnchor = -it.size.height / 2
        bottomAnchor = it.size.height / 2
      },
    contentAlignment = Alignment.Center,
  ) {
    key(bottomerItem) {
      bottomerItem?.let { movableContent(it) }
    }
    key(bottomItem) {
      bottomItem?.let { movableContent(it) }
    }
    key(topItem) {
      Box(
        Modifier.offset {
          offset.round()
        },
      ) {
        topItem?.let {
//                    Popup(alignment = Alignment.Center, offset = offset.round(), properties = PopupProperties()) {

          movableContent(it)
//                    }
        }
      }
    }
  }
}

// Calculate the closest anchor in 2 dimensions based on the current offset
private fun calculateClosestAnchor(
  offset: Offset,
  startAnchor: Int,
  endAnchor: Int,
  topAnchor: Int,
  bottomAnchor: Int,
): Int {
  val closestAnchor = when {
    offset.x < startAnchor -> startAnchor
    offset.x > endAnchor -> endAnchor
    offset.y < topAnchor -> topAnchor
    offset.y > bottomAnchor -> bottomAnchor
    else -> 0
  }
  return closestAnchor
}
