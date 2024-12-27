package com.isao.yfoo3.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.isao.yfoo3.App
import com.isao.yfoo3.core.designsystem.SplashScreenHost
import com.isao.yfoo3.core.theme.Yfoo2ThemeAndroid

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
      Yfoo2ThemeAndroid { colorScheme, typography ->
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
