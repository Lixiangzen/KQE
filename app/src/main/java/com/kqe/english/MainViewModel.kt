package com.kqe.english

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kqe.english.data.StudyStateEntity
import com.kqe.english.data.StudyUiState
import com.kqe.english.data.Word
import com.kqe.english.data.WordBook
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * 全局状态容器：设置 + 当前词库学习进度。
 * UI 层通过 collectAsState 订阅各 StateFlow。
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val wordRepo = (app as KqeApplication).wordRepository
    private val settings = (app as KqeApplication).settingsRepository
    private val dao = (app as KqeApplication).database.studyDao()
    private val gson = Gson()
    private val listType = object : TypeToken<List<String>>() {}.type

    val hasOnboarded: StateFlow<Boolean?> = settings.hasOnboarded
        .map<Boolean, Boolean?> { it }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val currentBook: StateFlow<WordBook> = settings.currentBookId
        .map { WordBook.fromId(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), WordBook.GAOZHONG)

    val dailyGoal: StateFlow<Int> = settings.dailyGoal
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 20)

    val studyState: StateFlow<StudyUiState?> = currentBook
        .flatMapLatest { book ->
            dao.observe(book.id).map { entity ->
                val total = wordRepo.getWords(book).size
                val today = LocalDate.now().toString()
                val learned = parseLearned(entity?.learnedJson)
                val reset = entity == null || entity.lastDate != today
                StudyUiState(
                    book = book,
                    learnedCount = learned.size,
                    todayCompleted = if (reset) 0 else entity.todayCompleted,
                    todayWrong = if (reset) 0 else entity.todayWrong,
                    totalWords = total
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ===== 词库数据访问（纯内存） =====
    fun wordsOf(book: WordBook): List<Word> = wordRepo.getWords(book)
    fun allWords(): List<Word> = wordRepo.allWords()
    fun search(query: String): List<Word> = wordRepo.search(query)

    // ===== 设置操作 =====
    fun selectBook(book: WordBook) {
        viewModelScope.launch { settings.setCurrentBook(book.id) }
    }

    fun setDailyGoal(goal: Int) {
        viewModelScope.launch { settings.setDailyGoal(goal) }
    }

    fun completeOnboarding() {
        viewModelScope.launch { settings.setOnboarded() }
    }

    // ===== 学习进度操作 =====
    fun recordAnswer(wordEn: String, correct: Boolean) {
        viewModelScope.launch {
            val book = currentBook.value
            val today = LocalDate.now().toString()
            val entity = dao.get(book.id)
            val reset = entity == null || entity.lastDate != today
            val completed = if (reset) 0 else entity?.todayCompleted ?: 0
            val wrong = if (reset) 0 else entity?.todayWrong ?: 0
            val learned = parseLearned(entity?.learnedJson).toMutableSet()
            if (correct) learned.add(wordEn)
            dao.upsert(
                StudyStateEntity(
                    bookId = book.id,
                    learnedJson = gson.toJson(learned.toList()),
                    todayCompleted = completed + 1,
                    todayWrong = if (correct) wrong else wrong + 1,
                    lastDate = today
                )
            )
        }
    }

    fun clearProgress(book: WordBook) {
        viewModelScope.launch { dao.clear(book.id) }
    }

    /**
     * 无尽练习答题：只累计「已学」与「答错」，不占用每日任务完成额度。
     */
    fun recordEndlessAnswer(wordEn: String, correct: Boolean) {
        viewModelScope.launch {
            val book = currentBook.value
            val today = LocalDate.now().toString()
            val entity = dao.get(book.id)
            val reset = entity == null || entity.lastDate != today
            val completed = if (reset) 0 else entity?.todayCompleted ?: 0
            val wrong = if (reset) 0 else entity?.todayWrong ?: 0
            val learned = parseLearned(entity?.learnedJson).toMutableSet()
            if (correct) learned.add(wordEn)
            dao.upsert(
                StudyStateEntity(
                    bookId = book.id,
                    learnedJson = gson.toJson(learned.toList()),
                    todayCompleted = completed,
                    todayWrong = if (correct) wrong else wrong + 1,
                    lastDate = today
                )
            )
        }
    }

    private fun parseLearned(json: String?): List<String> {
        return try {
            gson.fromJson<List<String>>(json ?: "[]", listType) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
