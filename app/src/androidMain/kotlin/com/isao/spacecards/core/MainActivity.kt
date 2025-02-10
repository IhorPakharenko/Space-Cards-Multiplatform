package com.isao.spacecards.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.isao.spacecards.App
import com.isao.spacecards.core.theme.AppThemeAndroid
import com.isao.spacecards.feature.designsystem.SplashScreenHost

class MainActivity :
  ComponentActivity(),
  SplashScreenHost {
  override var shouldKeepSplashScreen = true

  override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()
    splashScreen.setKeepOnScreenCondition { shouldKeepSplashScreen }

    super.onCreate(savedInstanceState)

    // Let the app take up all screen space, including system bars
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      AppThemeAndroid { colorScheme, typography ->
        App(
          colorScheme = colorScheme,
          typography = typography,
        )
      }
    }
  }
}

@Preview
@Composable
private fun AppAndroidPreview() {
  App()
}
