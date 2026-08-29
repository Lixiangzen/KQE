package com.kqe.english.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kqe.english.ui.components.AppIcons
import com.kqe.english.ui.components.FeatureCard
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.theme.Ink900

/**
 * 更多功能页：词典、无尽练习、设置、关于应用四个入口。
 */
@Composable
fun MoreScreen(
    onBack: () -> Unit,
    onDictionary: () -> Unit,
    onEndless: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(title = "更多功能", onBack = onBack)

        Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            FeatureCard(AppIcons.MenuBook, "词典", "查询任意单词释义与发音", onClick = onDictionary)
            Spacer(Modifier.height(16.dp))
            FeatureCard(AppIcons.Bolt, "无尽练习", "不限量随机刷题，双模式", onClick = onEndless)
            Spacer(Modifier.height(16.dp))
            FeatureCard(AppIcons.Settings, "设置", "更换词库与每日练习数量", onClick = onSettings)
            Spacer(Modifier.height(16.dp))
            FeatureCard(AppIcons.Info, "关于应用", "KQE 科启数码英语", onClick = onAbout)
        }
    }
}
