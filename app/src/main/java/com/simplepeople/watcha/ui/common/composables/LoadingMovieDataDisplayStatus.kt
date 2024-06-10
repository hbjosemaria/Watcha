package com.simplepeople.watcha.ui.common.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LoadingMovieDataImageDisplay(
    modifier: Modifier,
    image: Int,
    message: Int,
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(image),
            contentDescription = stringResource(id = message)
        )
        Text(
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = message)
        )
    }
}

@Composable
fun LoadingMovieDataLoadingDisplay(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier
    )
}