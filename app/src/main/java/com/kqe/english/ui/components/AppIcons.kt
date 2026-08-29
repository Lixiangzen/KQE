package com.kqe.english.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 统一图标集：全部采用白色线性（Outlined）图标，符合 TSC 视觉规范。
 * 使用完全限定名避免与对象属性命名冲突。
 */
object AppIcons {
    val ArrowBack: ImageVector get() = androidx.compose.material.icons.automirrored.outlined.ArrowBack
    val Menu: ImageVector get() = androidx.compose.material.icons.outlined.Menu
    val MenuBook: ImageVector get() = androidx.compose.material.icons.outlined.MenuBook
    val Bolt: ImageVector get() = androidx.compose.material.icons.outlined.Bolt
    val Settings: ImageVector get() = androidx.compose.material.icons.outlined.Settings
    val Info: ImageVector get() = androidx.compose.material.icons.outlined.Info
    val Search: ImageVector get() = androidx.compose.material.icons.outlined.Search
    val VolumeUp: ImageVector get() = androidx.compose.material.icons.outlined.VolumeUp
    val Check: ImageVector get() = androidx.compose.material.icons.outlined.Check
    val PlayArrow: ImageVector get() = androidx.compose.material.icons.outlined.PlayArrow
    val ArrowUp: ImageVector get() = androidx.compose.material.icons.outlined.KeyboardArrowUp
    val ArrowDown: ImageVector get() = androidx.compose.material.icons.outlined.KeyboardArrowDown
    val Delete: ImageVector get() = androidx.compose.material.icons.outlined.DeleteOutline
    val Refresh: ImageVector get() = androidx.compose.material.icons.outlined.Refresh
}
