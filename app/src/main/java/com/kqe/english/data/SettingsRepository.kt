package com.kqe.english.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "kqe_settings")

/**
 * 设置仓库：当前词库、每日练习数量、是否已完成首次选择，均为本地存储。
 */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val CURRENT_BOOK = stringPreferencesKey("current_book")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val HAS_ONBOARDED = booleanPreferencesKey("has_onboarded")
    }

    val currentBookId: Flow<String?> = context.dataStore.data
        .map { it[Keys.CURRENT_BOOK] }

    val dailyGoal: Flow<Int> = context.dataStore.data
        .map { it[Keys.DAILY_GOAL] ?: 20 }

    val hasOnboarded: Flow<Boolean> = context.dataStore.data
        .map { it[Keys.HAS_ONBOARDED] ?: false }

    suspend fun setCurrentBook(id: String) {
        context.dataStore.edit { it[Keys.CURRENT_BOOK] = id }
    }

    suspend fun setDailyGoal(goal: Int) {
        context.dataStore.edit { it[Keys.DAILY_GOAL] = goal }
    }

    suspend fun setOnboarded() {
        context.dataStore.edit { it[Keys.HAS_ONBOARDED] = true }
    }
}
