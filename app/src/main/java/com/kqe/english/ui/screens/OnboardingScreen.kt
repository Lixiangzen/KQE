package com.kqe.english.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.MainViewModel
import com.kqe.english.data.WordBook
import com.kqe.english.ui.components.KqeLogo
import com.kqe.english.ui.components.PrimaryButton
import com.kqe.english.ui.components.WordBookCard
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.White

/**
 * 首次启动页：展示 Logo 与「选择单词书」，三个词库卡片，选择后进入主页。
 */
@Composable
fun OnboardingScreen(
    viewModel: MainViewModel,
    onDone: () -> Unit
) {
    val currentBook by viewModel.currentBook.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        KqeLogo(72.dp)
        Spacer(Modifier.height(24.dp))
        Text("选择单词书", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("首次使用，请选择要学习的词库", color = GrayBlue, fontSize = 14.sp)
        Spacer(Modifier.height(28.dp))

        WordBook.entries.forEach { book ->
            WordBookCard(
                book = book,
                selected = book == currentBook,
                onClick = { viewModel.selectBook(book) }
            )
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(12.dp))
        PrimaryButton(
            text = "开始学习",
            onClick = {
                viewModel.completeOnboarding()
                onDone()
            }
        )
        Spacer(Modifier.height(24.dp))
        Text("出品方：科启数码", color = GrayBlue, fontSize = 12.sp)
        Spacer(Modifier.height(24.dp))
    }
}
