package com.kqe.english.data

/**
 * 单词数据模型，与 assets 下词库 JSON 字段一一对应。
 * 字段均有默认值，保证词表数据缺失某字段时不崩溃。
 */
data class Word(
    val en: String = "",
    val phonetic: String = "",
    val pos: String = "",
    val cn: String = "",
    val example: String = "",
    val exampleCn: String = ""
) {
    /** 释义展示：词性 + 中文，如 "n. 苹果" */
    val fullCn: String
        get() = if (pos.isNotBlank()) "$pos $cn" else cn
}
