package com.example.smsspoof.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.smsspoof.model.Sender
import kotlinx.coroutines.flow.Flow

@Dao
interface SenderDao {
    @Query("SELECT * FROM senders ORDER BY name")
    fun getAllSenders(): Flow<List<Sender>>

    @Query("SELECT * FROM senders WHERE id = :id")
    fun getSenderById(id: Long): Flow<Sender?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sender: Sender)

    @Update
    suspend fun update(sender: Sender)

    @Query("DELETE FROM senders WHERE id = :id")
    suspend fun deleteById(id: Long)
}
