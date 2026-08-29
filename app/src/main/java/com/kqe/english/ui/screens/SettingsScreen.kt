package com.kqe.english.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.MainViewModel
import com.kqe.english.data.WordBook
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.components.PressableContainer
import com.kqe.english.ui.components.WordBookCard
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.DangerRed
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy700
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White

private val GOAL_OPTIONS = listOf(10, 20, 30, 50)

/**
 * 设置页：更换单词书、设置每日练习数量、清除学习记录。
 */
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val currentBook by viewModel.currentBook.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(title = "设置", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            Text("当前单词书", color = GrayBlue, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
            WordBook.entries.forEach { book ->
                WordBookCard(
                    book = book,
                    selected = book == currentBook,
                    onClick = { viewModel.selectBook(book) }
                )
                Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(12.dp))
            Text("每日练习数量", color = GrayBlue, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                GOAL_OPTIONS.forEach { n ->
                    GoalButton(
                        value = n,
                        selected = n == dailyGoal,
                        onClick = { viewModel.setDailyGoal(n) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(10.dp))
                }
            }

            Spacer(Modifier.height(28.dp))
            PressableContainer(
                onClick = { showClearDialog = true }
            ) { pressed ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (pressed) Navy700 else Navy800)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("清除当前书学习记录", color = DangerRed, fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = Navy800,
            titleContentColor = White,
            textContentColor = GrayBlue,
            title = { Text("确认清除", fontWeight = FontWeight.Bold) },
            text = { Text("将清除「${currentBook.title}」的全部学习记录，此操作不可恢复。") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearProgress(currentBook)
                    showClearDialog = false
                }) { Text("清除", color = DangerRed) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("取消", color = GrayBlue) }
            }
        )
    }
}

@Composable
private fun GoalButton(
    value: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PressableContainer(modifier = modifier, onClick = onClick) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    when {
                        selected -> BrandBlue
                        pressed -> Navy700
                        else -> Navy800
                    }
                )
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("$value", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
