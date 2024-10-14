package org.thanhtd.currencyapp

import DarkColors
import LightColors
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.thanhtd.currencyapp.presentation.screen.HomeScreen

@Composable
@Preview
fun App() {
    val color = if (!isSystemInDarkTheme()) LightColors else DarkColors
    MaterialTheme(colorScheme = color) {
        Navigator(HomeScreen())
    }
}