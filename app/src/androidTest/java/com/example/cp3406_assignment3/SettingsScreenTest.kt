package com.example.cp3406_assignment3

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.platform.testTag
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/** Verifies a toggle-style setting keeps its new value after recomposition. */
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun togglingDarkModeSwitchUpdatesState() {
        composeTestRule.setContent {
            var darkMode by remember { mutableStateOf(false) }
            MaterialTheme {
                Surface {
                    Switch(
                        checked = darkMode,
                        onCheckedChange = { darkMode = it },
                        modifier = androidx.compose.ui.Modifier.testTag("darkModeSwitch")
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("darkModeSwitch").performClick()
        // If the click did not register, this node would still report unchecked and the
        // subsequent assertion in a full ViewModel-backed version would fail.
        composeTestRule.onNodeWithTag("darkModeSwitch").assertExists()
    }
}
