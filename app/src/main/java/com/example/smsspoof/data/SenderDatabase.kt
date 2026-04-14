package com.example.smsspoof.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smsspoof.model.Sender

@Database(entities = [Sender::class], version = 1, exportSchema = false)
abstract class SenderDatabase : RoomDatabase() {
    abstract fun senderDao(): SenderDao

    companion object {
        @Volatile
        private var INSTANCE: SenderDatabase? = null

        fun getDatabase(context: Context): SenderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SenderDatabase::class.java,
                    "sender_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
