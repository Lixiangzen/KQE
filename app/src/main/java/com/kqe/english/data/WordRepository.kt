package com.kqe.english.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 词库仓库：从 assets 读取并解析词库 JSON，内存缓存。
 * 替换完整词表只需覆盖 assets 下同名 JSON 文件，字段结构保持不变。
 */
class WordRepository(private val context: Context) {

    private val gson = Gson()
    private val cache = mutableMapOf<WordBook, List<Word>>()
    private val listType = object : TypeToken<List<Word>>() {}.type

    /** 获取指定词库的全部单词（首次访问时从 assets 加载并缓存） */
    fun getWords(book: WordBook): List<Word> {
        return cache.getOrPut(book) { load(book) }
    }

    /** 三个词库全部合并去重（用于词典搜索） */
    fun allWords(): List<Word> {
        return WordBook.entries.flatMap { getWords(it) }
    }

    /** 词典搜索：按英文前缀/包含匹配，结果去重 */
    fun search(query: String): List<Word> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        val seen = HashSet<String>()
        return allWords().filter { word ->
            word.en.lowercase().contains(q) && seen.add(word.en.lowercase())
        }
    }

    private fun load(book: WordBook): List<Word> {
        return try {
            val json = context.assets.open(book.assetFile)
                .bufferedReader(Charsets.UTF_8).use { it.readText() }
            gson.fromJson<List<Word>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
