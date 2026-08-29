package com.kqe.english.data

/**
 * 单个词库的仪表盘 UI 状态。
 * totalWords 取实际加载到的词条数，保证进度可真实完成；
 * 词库说明文案（如"高中 3500 词"）由 WordBook.subtitle 提供。
 */
data class StudyUiState(
    val book: WordBook,
    val learnedCount: Int = 0,
    val todayCompleted: Int = 0,
    val todayWrong: Int = 0,
    val totalWords: Int = 0
) {
    val remain: Int get() = (totalWords - learnedCount).coerceAtLeast(0)
}
