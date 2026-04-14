package com.example.smsspoof.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "senders")
data class Sender(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val senderId: String,
    val isEnabled: Boolean = true
)
