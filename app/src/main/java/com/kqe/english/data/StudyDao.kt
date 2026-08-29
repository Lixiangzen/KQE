package com.kqe.english.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    @Query("SELECT * FROM study_state WHERE bookId = :bookId")
    fun observe(bookId: String): Flow<StudyStateEntity?>

    @Query("SELECT * FROM study_state WHERE bookId = :bookId")
    suspend fun get(bookId: String): StudyStateEntity?

    @Upsert
    suspend fun upsert(state: StudyStateEntity)

    @Query("DELETE FROM study_state WHERE bookId = :bookId")
    suspend fun clear(bookId: String)
}
