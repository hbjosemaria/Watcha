package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ColumnElementDivider(
    color: Color = Color.Gray,
) {
    HorizontalDivider(
        thickness = 1.dp,
        color = color,
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
    )
}