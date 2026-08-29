package com.kqe.english.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 统一图标集：全部采用白色线性（Outlined）图标，符合 TSC 视觉规范。
 */
object AppIcons {
    val ArrowBack: ImageVector get() = Icons.AutoMirrored.Outlined.ArrowBack
    val Menu: ImageVector get() = Icons.Outlined.Menu
    val MenuBook: ImageVector get() = Icons.Outlined.MenuBook
    val Bolt: ImageVector get() = Icons.Outlined.Bolt
    val Settings: ImageVector get() = Icons.Outlined.Settings
    val Info: ImageVector get() = Icons.Outlined.Info
    val Search: ImageVector get() = Icons.Outlined.Search
    val VolumeUp: ImageVector get() = Icons.Outlined.VolumeUp
    val Check: ImageVector get() = Icons.Outlined.Check
    val PlayArrow: ImageVector get() = Icons.Outlined.PlayArrow
    val ArrowUp: ImageVector get() = Icons.Outlined.KeyboardArrowUp
    val ArrowDown: ImageVector get() = Icons.Outlined.KeyboardArrowDown
    val Delete: ImageVector get() = Icons.Outlined.DeleteOutline
    val Refresh: ImageVector get() = Icons.Outlined.Refresh
}
