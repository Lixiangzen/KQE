package com.kqe.english.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.MainViewModel
import com.kqe.english.data.Word
import com.kqe.english.ui.components.AppIcons
import com.kqe.english.ui.components.IconButton
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.components.PressableContainer
import com.kqe.english.ui.components.PrimaryButton
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.CorrectBg
import com.kqe.english.ui.theme.CorrectBorder
import com.kqe.english.ui.theme.DangerRed
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy700
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White
import com.kqe.english.ui.theme.WrongBg
import com.kqe.english.ui.theme.WrongBorder
import com.kqe.english.util.TtsHelper

private enum class EndlessMode(val label: String) {
    EN2CN("看英文选中文"),
    CN2EN("看中文选英文")
}

private data class EndlessQuestion(
    val word: Word,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int
)

private fun nextQuestion(words: List<Word>, mode: EndlessMode): EndlessQuestion {
    val word = words.random()
    return if (mode == EndlessMode.EN2CN) {
        val correct = word.fullCn
        val distractors = words.filter { it.fullCn.isNotBlank() && it.fullCn != correct }
            .map { it.fullCn }.distinct().shuffled().take(3)
        val options = (listOf(correct) + distractors).shuffled()
        EndlessQuestion(word, word.en, options, options.indexOf(correct))
    } else {
        val correct = word.en
        val distractors = words.filter { it.en != correct }
            .map { it.en }.distinct().shuffled().take(3)
        val options = (listOf(correct) + distractors).shuffled()
        EndlessQuestion(word, word.fullCn, options, options.indexOf(correct))
    }
}

/**
 * 无尽练习页：不限数量的随机刷题，支持看英文选中文 / 看中文选英文。
 */
@Composable
fun EndlessScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tts = remember { TtsHelper(context) }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    val currentBook by viewModel.currentBook.collectAsState()
    val words = remember(currentBook) { viewModel.wordsOf(currentBook) }

    var mode by remember { mutableStateOf(EndlessMode.EN2CN) }
    var question by remember(words) {
        mutableStateOf(if (words.isNotEmpty()) nextQuestion(words, EndlessMode.EN2CN) else null)
    }
    var selected by remember { mutableIntStateOf(-1) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(title = "无尽练习", onBack = onBack)

        // 模式切换
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            EndlessMode.entries.forEach { m ->
                val isSelected = m == mode
                PressableContainer(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (m != mode && words.isNotEmpty()) {
                            mode = m
                            question = nextQuestion(words, m)
                            selected = -1
                            correctCount = 0
                            wrongCount = 0
                        }
                    }
                ) { pressed ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) BrandBlue else if (pressed) Navy700 else Navy800)
                            .then(
                                if (isSelected) Modifier.border(1.dp, BrandBlue, RoundedCornerShape(12.dp))
                                else Modifier
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(m.label, color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(10.dp))
            }
        }

        // 统计
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("正确 $correctCount", color = GrayBlue, fontSize = 14.sp)
            Spacer(Modifier.weight(1f))
            Text("错误 $wrongCount", color = DangerRed, fontSize = 14.sp)
            Spacer(Modifier.weight(1f))
            Text("已练 ${correctCount + wrongCount}", color = GrayBlue, fontSize = 14.sp, textAlign = TextAlign.End)
        }

        val q = question
        if (q == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("词库暂无单词，请先更换词库", color = GrayBlue, fontSize = 14.sp)
            }
            return@Column
        }

        val answered = selected >= 0

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // 题目卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Navy800)
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(q.prompt, color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    if (mode == EndlessMode.EN2CN) {
                        Spacer(Modifier.width(12.dp))
                        IconButton(AppIcons.VolumeUp, size = 36.dp, onClick = { tts.speak(q.word.en) })
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    if (mode == EndlessMode.EN2CN) "请选择正确释义" else "请选择正确英文",
                    color = GrayBlue,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            q.options.forEachIndexed { index, option ->
                EndlessOption(
                    text = option,
                    isCorrect = index == q.correctIndex,
                    isWrong = answered && index == selected && index != q.correctIndex,
                    answered = answered,
                    onClick = {
                        if (!answered) {
                            selected = index
                            val correct = index == q.correctIndex
                            if (correct) correctCount++ else wrongCount++
                            viewModel.recordEndlessAnswer(q.word.en, correct)
                        }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        PrimaryButton(
            text = "下一题",
            enabled = answered,
            onClick = {
                question = nextQuestion(words, mode)
                selected = -1
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            height = 52.dp
        )
    }
}

@Composable
private fun EndlessOption(
    text: String,
    isCorrect: Boolean,
    isWrong: Boolean,
    answered: Boolean,
    onClick: () -> Unit
) {
    val bg = when {
        !answered -> Navy800
        isCorrect -> CorrectBg
        isWrong -> WrongBg
        else -> Navy800
    }
    val border: Color? = when {
        !answered -> null
        isCorrect -> CorrectBorder
        isWrong -> WrongBorder
        else -> null
    }
    PressableContainer(onClick = onClick) { pressed ->
        val effectiveBg = if (!answered && pressed) Navy700 else bg
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(effectiveBg)
                .then(border?.let { Modifier.border(2.dp, it, RoundedCornerShape(14.dp)) } ?: Modifier)
                .padding(vertical = 18.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = White,
                fontSize = 16.sp,
                fontWeight = if (isCorrect && answered) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}
