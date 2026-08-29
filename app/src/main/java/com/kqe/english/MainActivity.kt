package com.kqe.english

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kqe.english.ui.navigation.AppNavHost
import com.kqe.english.ui.navigation.Routes
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.KqeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KqeTheme {
                AdaptiveDensity {
                    KqeApp()
                }
            }
        }
    }
}

/**
 * 华强北手表方形/小屏自适应：屏幕过窄时整体缩放 dp，保证布局不溢出。
 */
@Composable
private fun AdaptiveDensity(content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val baseDensity = LocalDensity.current
    val widthDp = configuration.screenWidthDp
    val scale = when {
        widthDp < 180 -> 0.72f
        widthDp < 240 -> 0.82f
        widthDp < 300 -> 0.92f
        else -> 1f
    }
    if (scale >= 1f) {
        content()
    } else {
        val scaledDensity = Density(baseDensity.density * scale, baseDensity.fontScale * scale)
        CompositionLocalProvider(LocalDensity provides scaledDensity) {
            content()
        }
    }
}

@Composable
private fun KqeApp(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val hasOnboarded by viewModel.hasOnboarded.collectAsState()

    when (hasOnboarded) {
        null -> {
            // 首次加载设置，显示空白背景避免闪烁
            Box(Modifier.fillMaxSize().background(Ink900))
        }
        else -> {
            AppNavHost(
                navController = navController,
                viewModel = viewModel,
                startDestination = if (hasOnboarded == true) Routes.HOME else Routes.ONBOARDING
            )
        }
    }
}
