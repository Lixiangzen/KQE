package com.kqe.english.data

/**
 * 内置词库定义。
 * total 为完整词库目标词数（用于进度统计的分母），assets 中为可替换的精选词条。
 */
enum class WordBook(
    val id: String,
    val title: String,
    val subtitle: String,
    val total: Int,
    val assetFile: String
) {
    CHUZHONG("chuzhong", "初中英语", "初中 1600 词", 1600, "words_chuzhong.json"),
    GAOZHONG("gaozhong", "高中英语", "高中 3500 词", 3500, "words_gaozhong.json"),
    GAOSAN("gaosan", "高三重点英语", "高三 1200 高频词", 1200, "words_gaosan.json");

    companion object {
        fun fromId(id: String?): WordBook =
            entries.firstOrNull { it.id == id } ?: GAOZHONG
    }
}
