package com.simplepeople.watcha.ui.common.composables.topbar.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.topbar.HomeFilterOptions

/**
 * TopBar buttons for Home
 */
@Composable
fun TopAppBarButton(
    buttonFilterOption: HomeFilterOptions,
    selectedFilterOption: HomeFilterOptions,
    text: Int,
    action: () -> Unit
) {

    val buttonColor by animateColorAsState(
        targetValue = if (buttonFilterOption == selectedFilterOption) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
        animationSpec = tween(durationMillis = 250),
        label = "Button animation"
    )

    val contentColor by animateColorAsState(
        targetValue = if (buttonFilterOption == selectedFilterOption) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
        animationSpec = tween(durationMillis = 250),
        label = "Button animation"
    )

    Button(
        onClick = {
            action()
        },
        contentPadding = ButtonDefaults.TextButtonContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = contentColor,
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .padding(2.dp, 0.dp, 2.dp, 0.dp)
    ) {
        Text(
            text = stringResource(id = text),
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        )
    }
}

/**
 * TopBar logo composable
 */
@Composable
fun TopAppBarLogo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_main_screen),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .size(120.dp)
        )
    }
}

/**
 * TopBar text composable
 */
@Composable
fun TopAppBarText(
    modifier: Modifier = Modifier,
    textReference: Int
) {
    Text(
        modifier = modifier,
        text = stringResource(id = textReference)
    )
}