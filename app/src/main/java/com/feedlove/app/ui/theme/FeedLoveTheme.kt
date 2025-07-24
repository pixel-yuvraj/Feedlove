// com/feedlove/app/ui/theme/FeedloveTheme.kt
package com.feedlove.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// FeedLove Orange Palette
val OrangePrimary   = Color(0xFFF66103) // Main brand orange
val OrangeSecondary = Color(0xFFEFD306) // Accent yellow
val DarkText        = Color(0xFF081833) // Deep navy for contrast

private val LightColorScheme = lightColorScheme(
    primary        = OrangePrimary,
    onPrimary      = Color.White,
    secondary      = OrangeSecondary,
    onSecondary    = DarkText,
    background     = Color(0xFFFFF9F3),
    onBackground   = DarkText,
    surface        = Color.White,
    onSurface      = DarkText,
    error          = Color(0xFFB00020),
    onError        = Color.White
)

@Composable
fun FeedloveTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = FeedloveTypography,
        shapes      = Shapes(
            small  = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large  = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}
