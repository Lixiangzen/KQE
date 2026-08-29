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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.MainViewModel
import com.kqe.english.ui.components.AppIcons
import com.kqe.english.ui.components.IconButton
import com.kqe.english.ui.components.KqeProgressBar
import com.kqe.english.ui.components.PrimaryButton
import com.kqe.english.ui.components.VDivider
import com.kqe.english.ui.theme.DangerRed
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White

/**
 * 主学习页：学习仪表盘。
 */
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onPractice: () -> Unit,
    onMore: () -> Unit
) {
    val state by viewModel.studyState.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
            .statusBarsPadding()
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "KQE",
                color = White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                icon = AppIcons.Menu,
                onClick = onMore,
                size = 40.dp,
                contentDescription = "更多功能"
            )
        }

        val s = state
        if (s == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("加载中…", color = GrayBlue, fontSize = 14.sp)
            }
        } else {
            val done = s.todayCompleted
            val goal = dailyGoal
            val remain = (goal - done).coerceAtLeast(0)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(4.dp))

                // 单词书信息卡
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Navy800)
                        .padding(horizontal = 20.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("单词书", color = GrayBlue, fontSize = 14.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(s.book.title, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(s.book.subtitle, color = GrayBlue, fontSize = 14.sp)
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(s.learnedCount.toString(), "已学", Modifier.weight(1f))
                        VDivider()
                        StatItem(s.totalWords.toString(), "总词数", Modifier.weight(1f))
                        VDivider()
                        StatItem(s.remain.toString(), "剩余", Modifier.weight(1f))
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 每日任务卡
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(Navy800)
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Text(
                        "每日任务",
                        color = GrayBlue,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Text("今日 $done / $goal", color = GrayBlue, fontSize = 15.sp)
                        Spacer(Modifier.weight(1f))
                        Text(
                            "答错 ${s.todayWrong}",
                            color = if (s.todayWrong > 0) DangerRed else GrayBlue,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    KqeProgressBar(progress = if (goal > 0) done.toFloat() / goal else 0f)
                    Spacer(Modifier.height(14.dp))
                    Text(
                        if (remain <= 0) "今日任务已完成🎉" else "今日还剩 $remain 个单词，加油😊",
                        color = White,
                        fontSize = 15.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(16.dp))
            }

            Spacer(Modifier.weight(1f))

            // 底部主按钮
            val todayDone = done >= dailyGoal
            PrimaryButton(
                text = if (todayDone) "今日已完成" else "继续学习",
                icon = if (todayDone) null else AppIcons.PlayArrow,
                enabled = !todayDone,
                onClick = onPractice,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(label, color = GrayBlue, fontSize = 13.sp)
    }
}
