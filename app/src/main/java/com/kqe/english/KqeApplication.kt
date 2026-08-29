package com.kqe.english

import android.app.Application
import com.kqe.english.data.AppDatabase
import com.kqe.english.data.SettingsRepository
import com.kqe.english.data.WordRepository

/**
 * 应用入口，持有全局单例仓库。
 */
class KqeApplication : Application() {
    val wordRepository by lazy { WordRepository(this) }
    val settingsRepository by lazy { SettingsRepository(this) }
    val database by lazy { AppDatabase.get(this) }
}
