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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.MainViewModel
import com.kqe.english.data.Word
import com.kqe.english.ui.components.AppIcons
import com.kqe.english.ui.components.IconButton
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.Divider
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White
import com.kqe.english.util.TtsHelper

/**
 * 词典页：搜索查询任意单词的释义与发音。
 */
@Composable
fun DictionaryScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tts = remember { TtsHelper(context) }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    var query by remember { mutableStateOf("") }
    val results = remember(query) { viewModel.search(query) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(title = "词典", onBack = onBack)

        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("搜索单词", color = GrayBlue) },
            singleLine = true,
            leadingIcon = { Icon(AppIcons.Search, null, tint = GrayBlue) },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Navy800,
                unfocusedContainerColor = Navy800,
                focusedTextColor = White,
                unfocusedTextColor = White,
                cursorColor = BrandBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = GrayBlue,
                unfocusedLeadingIconColor = GrayBlue
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (query.isBlank()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("输入英文单词进行查询", color = GrayBlue, fontSize = 14.sp)
            }
        } else if (results.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("未找到相关单词", color = GrayBlue, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp
                )
            ) {
                items(results, key = { it.en }) { word ->
                    WordItem(word = word, onSpeak = { tts.speak(word.en) })
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun WordItem(word: Word, onSpeak: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Navy800)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(word.en, color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            IconButton(
                icon = AppIcons.VolumeUp,
                size = 36.dp,
                onClick = onSpeak
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(word.phonetic, color = GrayBlue, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(Divider))
        Spacer(Modifier.height(8.dp))
        Text(word.fullCn, color = White, fontSize = 16.sp)
        if (word.example.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(word.example, color = GrayBlue, fontSize = 14.sp)
            if (word.exampleCn.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(word.exampleCn, color = GrayBlue, fontSize = 13.sp)
            }
        }
    }
}
