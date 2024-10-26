package com.isao.yfoo3.liked.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isao.yfoo3.resources.Res
import com.isao.yfoo3.resources.added
import org.jetbrains.compose.resources.stringResource

@Composable
fun LikedGridSettings(
    sortAscending: Boolean,
    setSortAscending: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(horizontal = 8.dp)
) {
    TextButton(
        onClick = { setSortAscending(!sortAscending) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(text = stringResource(Res.string.added))
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            imageVector = if (sortAscending) {
                Icons.Filled.ArrowUpward
            } else {
                Icons.Filled.ArrowDownward
            },
            contentDescription = null,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}