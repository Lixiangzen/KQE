package com.kqe.english.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 单个词库的学习状态。按 bookId 一行存储。
 * learnedJson 存已学单词英文的 JSON 数组（跨天累计）。
 * todayCompleted / todayWrong 为当日数据，lastDate 用于每日零点重置判断。
 */
@Entity(tableName = "study_state")
data class StudyStateEntity(
    @PrimaryKey val bookId: String,
    val learnedJson: String = "[]",
    val todayCompleted: Int = 0,
    val todayWrong: Int = 0,
    val lastDate: String = ""
)
