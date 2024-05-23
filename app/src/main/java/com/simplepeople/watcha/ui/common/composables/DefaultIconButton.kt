package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DefaultIconButton(
    action: () -> Unit,
    iconImage: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color? = null
) {
    IconButton(
        onClick = {
            action()
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = iconImage,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize(),
            tint = tint ?: LocalContentColor.current
        )
    }
}