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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import com.kqe.english.ui.components.cardSurface
import com.kqe.english.ui.components.AppIcons
import com.kqe.english.ui.components.IconButton
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.components.PressableContainer
import com.kqe.english.ui.components.PrimaryButton
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.CardHighlight
import com.kqe.english.ui.theme.CorrectBg
import com.kqe.english.ui.theme.CorrectBorder
import com.kqe.english.ui.theme.DangerRed
import com.kqe.english.ui.theme.Divider
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy700
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White
import com.kqe.english.ui.theme.WrongBg
import com.kqe.english.ui.theme.WrongBorder
import com.kqe.english.util.TtsHelper

private data class Question(val word: Word, val options: List<String>, val correctIndex: Int)

private enum class OptionState { NORMAL, CORRECT, WRONG }

private fun generateQuestions(words: List<Word>, count: Int): List<Question> {
    if (words.isEmpty()) return emptyList()
    return words.shuffled().take(count.coerceAtLeast(1)).map { word ->
        val correct = word.fullCn
        val distractors = words.asSequence()
            .filter { it.fullCn.isNotBlank() && it.fullCn != correct }
            .map { it.fullCn }
            .distinct()
            .shuffled()
            .take(3)
            .toList()
        val options = (listOf(correct) + distractors).shuffled()
        Question(word, options, options.indexOf(correct))
    }
}

/**
 * 单词练习页：四选一释义答题。
 */
@Composable
fun PracticeScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tts = remember { TtsHelper(context) }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    val currentBook by viewModel.currentBook.collectAsState()
    val dailyGoal by viewModel.dailyGoal.collectAsState()

    val words = remember(currentBook) { viewModel.wordsOf(currentBook) }
    var questions by remember(currentBook, dailyGoal) {
        mutableStateOf(generateQuestions(words, minOf(dailyGoal, words.size)))
    }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(
            title = "单词练习",
            onBack = onBack,
            actions = {
                Column {
                    IconButton(AppIcons.ArrowUp, size = 32.dp, onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            selected = -1
                        }
                    })
                    Spacer(Modifier.height(2.dp))
                    IconButton(AppIcons.ArrowDown, size = 32.dp, onClick = {
                        if (currentIndex < questions.size - 1) {
                            currentIndex++
                            selected = -1
                        }
                    })
                }
            }
        )

        if (questions.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("词库暂无单词，请先更换词库", color = GrayBlue, fontSize = 14.sp)
            }
            return@Column
        }

        val q = questions[currentIndex]
        val answered = selected >= 0

        // 统计栏
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
            Text(
                "${currentIndex + 1} / ${questions.size}",
                color = GrayBlue,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // 单词卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardSurface(RoundedCornerShape(20.dp))
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(q.word.en, color = White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    IconButton(
                        icon = AppIcons.VolumeUp,
                        size = 36.dp,
                        onClick = { tts.speak(q.word.en) }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(q.word.phonetic, color = GrayBlue, fontSize = 15.sp)
                Spacer(Modifier.height(12.dp))
                Text("请选择正确释义", color = GrayBlue, fontSize = 13.sp)
            }

            Spacer(Modifier.height(16.dp))

            // 四个选项
            q.options.forEachIndexed { index, option ->
                val state = when {
                    !answered -> OptionState.NORMAL
                    index == q.correctIndex -> OptionState.CORRECT
                    index == selected -> OptionState.WRONG
                    else -> OptionState.NORMAL
                }
                OptionCard(
                    text = option,
                    state = state,
                    onClick = {
                        if (!answered) {
                            selected = index
                            val correct = index == q.correctIndex
                            if (correct) correctCount++ else wrongCount++
                            viewModel.recordAnswer(q.word.en, correct)
                        }
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            // 答案解析卡（答错时展示）
            if (answered && selected != q.correctIndex) {
                Spacer(Modifier.height(4.dp))
                AnswerCard(word = q.word, onSpeak = { tts.speak(q.word.en) })
            }

            Spacer(Modifier.height(16.dp))

            // 下一题按钮（紧跟选项/答案卡，随内容滚动）
            PrimaryButton(
                text = if (currentIndex < questions.size - 1) "下一题" else "完成练习",
                enabled = answered,
                onClick = {
                    if (currentIndex < questions.size - 1) {
                        currentIndex++
                        selected = -1
                    } else {
                        onBack()
                    }
                },
                height = 52.dp
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OptionCard(
    text: String,
    state: OptionState,
    onClick: () -> Unit
) {
    val bg: Color = when (state) {
        OptionState.NORMAL -> Navy800
        OptionState.CORRECT -> CorrectBg
        OptionState.WRONG -> WrongBg
    }
    val borderColor: Color? = when (state) {
        OptionState.NORMAL -> null
        OptionState.CORRECT -> CorrectBorder
        OptionState.WRONG -> WrongBorder
    }
    val fontWeight: FontWeight = when (state) {
        OptionState.NORMAL -> FontWeight.Normal
        OptionState.CORRECT -> FontWeight.Bold
        OptionState.WRONG -> FontWeight.Normal
    }
    PressableContainer(onClick = onClick) { pressed ->
        val effectiveBg = if (state == OptionState.NORMAL && pressed) Navy700 else bg
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(effectiveBg)
                .then(
                    borderColor?.let {
                        Modifier.border(2.dp, it, RoundedCornerShape(14.dp))
                    } ?: Modifier.border(1.dp, CardHighlight, RoundedCornerShape(14.dp))
                )
                .padding(vertical = 18.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                color = White,
                fontSize = 16.sp,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AnswerCard(word: Word, onSpeak: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .cardSurface(RoundedCornerShape(16.dp))
            .border(1.5.dp, WrongBorder, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text("正确答案", color = DangerRed, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(word.en, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            IconButton(
                icon = AppIcons.VolumeUp,
                size = 36.dp,
                onClick = onSpeak
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(word.phonetic, color = GrayBlue, fontSize = 14.sp)
        Spacer(Modifier.height(10.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
        Spacer(Modifier.height(10.dp))
        Text("释义", color = GrayBlue, fontSize = 13.sp)
        Spacer(Modifier.height(2.dp))
        Text(word.fullCn, color = White, fontSize = 16.sp)
    }
}
