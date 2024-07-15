package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DefaultIconButton(
    action: () -> Unit,
    iconImage: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    IconButton(
        onClick = action,
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

@Composable
fun DefaultTextIconButton(
    action: () -> Unit,
    iconImage: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    text: Int,
) {
    ElevatedButton(
        onClick = action,
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 15.dp
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = iconImage,
                contentDescription = contentDescription,
                tint = tint ?: LocalContentColor.current
            )
            Text(
                modifier = Modifier
                    .padding(
                        start = 6.dp
                    ),
                text = stringResource(id = text),
                textAlign = TextAlign.Center
            )
        }
    }
}