package com.bivizul.phonecam.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column {

        Button(onClick = onClick) {
            Text(text = "Button 1")
        }
    }
}

@Preview(name = "MainScreen")
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onClick = {}
    )
}