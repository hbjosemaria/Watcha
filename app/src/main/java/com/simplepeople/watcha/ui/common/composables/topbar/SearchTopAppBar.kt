package com.simplepeople.watcha.ui.common.composables.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simplepeople.watcha.R
import com.simplepeople.watcha.ui.common.composables.DefaultIconButton

/**
 * TopBar made for search purposes. It has a TextField inside.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    searchText: String,
    onValueChange: (String) -> Unit,
    cleanSearch: () -> Unit,
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    CenterAlignedTopAppBar(
        modifier = Modifier
            .padding(bottom = 6.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        scrollBehavior = scrollBehavior,
        title = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            keyboardController?.show()
                        }
                    },
                shape = SearchBarDefaults.inputFieldShape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent

                ),
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                singleLine = true,
                value = searchText,
                onValueChange = { newText ->
                    onValueChange(newText)
                },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                label = { Text(stringResource(R.string.search_label)) },
                trailingIcon = {
                    if (searchText.isNotBlank()) {
                        DefaultIconButton(
                            action = cleanSearch,
                            iconImage = Icons.Filled.Clear,
                            contentDescription = Icons.Filled.Clear.name,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            )
        }
    )

}