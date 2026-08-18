package com.nezco.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val container = document.getElementById("compose-target") ?: document.body!!
    ComposeViewport(container) {
        App()
    }
}
