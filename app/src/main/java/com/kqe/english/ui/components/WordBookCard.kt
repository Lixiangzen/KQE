package com.kqe.english.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.data.WordBook
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Navy700
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.White

/**
 * 词库选择卡片：深藏青底，选中态主蓝描边 + 右上角白色对勾。
 */
@Composable
fun WordBookCard(
    book: WordBook,
    selected: Boolean,
    onClick: () -> Unit
) {
    PressableContainer(onClick = onClick) { pressed ->
        val bg = if (pressed) Navy700 else Navy800
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(bg)
                .then(
                    if (selected) Modifier.border(2.dp, BrandBlue, RoundedCornerShape(18.dp))
                    else Modifier
                )
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(book.title, color = White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(book.subtitle, color = GrayBlue, fontSize = 14.sp)
            }
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(22.dp)
                        .background(BrandBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        AppIcons.Check,
                        contentDescription = "已选",
                        tint = White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/** 功能入口卡片：左侧图标底 + 标题 + 副标题 */
@Composable
fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconColor: Color = BrandBlue
) {
    PressableContainer(onClick = onClick) { pressed ->
        val bg = if (pressed) Navy700 else Navy800
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(bg)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = White, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.size(16.dp))
                Column {
                    Text(title, color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                    Text(subtitle, color = GrayBlue, fontSize = 13.sp)
                }
            }
        }
    }
}
