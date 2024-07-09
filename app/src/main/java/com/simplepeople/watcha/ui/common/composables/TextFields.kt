package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TextFieldClickableSelected(
    value: String,
    labelText: String = "",
    action: () -> Unit,
    iconVector: ImageVector,
) {
    val colors = TextFieldDefaults.colors(
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
        disabledTextColor = MaterialTheme.colorScheme.onTertiary,
        disabledLabelColor = MaterialTheme.colorScheme.onSecondary,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onTertiary
    )

    TextFieldClickable(
        value = value,
        action = action,
        colors = colors,
        iconVector = iconVector,
        labelText = labelText
    )
}

@Composable
fun TextFieldClickableUnselected(
    value: String,
    labelText: String = "",
    action: () -> Unit,
    iconVector: ImageVector,
) {
    val colors = TextFieldDefaults.colors(
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        disabledTextColor = MaterialTheme.colorScheme.onBackground,
        disabledLabelColor = MaterialTheme.colorScheme.tertiary,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground
    )

    TextFieldClickable(
        value = value,
        action = action,
        colors = colors,
        iconVector = iconVector,
        labelText = labelText
    )
}


@Composable
private fun TextFieldClickable(
    value: String,
    labelText: String,
    action: () -> Unit,
    colors: TextFieldColors,
    iconVector: ImageVector,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                action()
            },
        enabled = false,
        label = if (labelText.isNotEmpty()) {
            {
                Text(
                    text = labelText
                )
            }
        } else null,
        value = value,
        onValueChange = {},
        readOnly = true,
        leadingIcon = {
            Icon(
                imageVector = iconVector,
                contentDescription = iconVector.name
            )
        },
        colors = colors
    )
}