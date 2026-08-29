package com.kqe.english.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kqe.english.ui.theme.BrandBlue
import com.kqe.english.ui.theme.GrayBlue
import com.kqe.english.ui.theme.Navy700
import com.kqe.english.ui.theme.Navy800
import com.kqe.english.ui.theme.ProgressTrack
import com.kqe.english.ui.theme.White

/**
 * 可缩放点击容器：按压时缩放到 0.98，过渡 150ms，并向子内容传递按压态用于背景加深。
 */
@Composable
fun PressableContainer(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable (isPressed: Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = tween(150),
        label = "pressScale"
    )
    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        content(pressed)
    }
}

/** 圆角方形图标按钮（顶部返回/菜单/发音等） */
@Composable
fun IconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    contentDescription: String? = null
) {
    PressableContainer(modifier = modifier.size(size), onClick = onClick) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp))
                .background(if (pressed) Navy700 else Navy800),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription, tint = White, modifier = Modifier.size(size * 0.5f))
        }
    }
}

/** 顶部栏：左侧返回 + 居中标题 + 右侧操作区 */
@Composable
fun KqeTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(52.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                icon = AppIcons.ArrowBack,
                onClick = onBack,
                size = 36.dp,
                contentDescription = "返回"
            )
        }
        Text(
            text = title,
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        actions()
    }
}

/** 品牌 Logo：主蓝圆角底 + 白色 KQE 字样 */
@Composable
fun KqeLogo(size: Dp = 72.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(20.dp))
            .background(BrandBlue),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "KQE",
            color = White,
            fontSize = (size.value * 0.34f).sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

/** 胶囊形进度条 */
@Composable
fun KqeProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(ProgressTrack)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(BrandBlue, RoundedCornerShape(5.dp))
        )
    }
}

/** 主蓝主按钮 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    height: Dp = 56.dp
) {
    PressableContainer(
        modifier = modifier.fillMaxWidth().height(height),
        onClick = { if (enabled) onClick() }
    ) { pressed ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    when {
                        !enabled -> Navy800
                        pressed -> BrandBlue.copy(alpha = 0.82f)
                        else -> BrandBlue
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, null, tint = if (enabled) White else GrayBlue)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = if (enabled) White else GrayBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/** 竖向细分隔线 */
@Composable
fun VDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(32.dp)
            .background(ProgressTrack)
    )
}
