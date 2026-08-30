package com.kqe.english.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.ui.components.cardSurface
import com.kqe.english.ui.components.KqeLogo
import com.kqe.english.ui.components.KqeTopBar
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Ink900
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White

/**
 * 关于应用页：品牌卡片 + 应用简介 + 出品方。
 */
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink900)
    ) {
        KqeTopBar(title = "关于应用", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            KqeLogo(80.dp)
            Spacer(Modifier.height(20.dp))
            Text("KQE 科启数码英语", color = White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("KeQi English", color = GrayBlue, fontSize = 14.sp)

            Spacer(Modifier.height(28.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardSurface(RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Text("应用简介", color = GrayBlue, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                Text(
                    "KQE 是一款极简无广告的英语背单词应用，内置初中、高中、高三重点三大词库，" +
                        "支持四选一练习、词典查询与无尽刷题，帮助你在碎片时间高效记单词。",
                    color = White,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }

            Spacer(Modifier.height(28.dp))
            Text("出品方：科启数码", color = GrayBlue, fontSize = 12.sp)
            Spacer(Modifier.height(24.dp))
        }
    }
}
