package com.example.smsspoof.data

import com.example.smsspoof.model.Sender
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SenderRepository @Inject constructor(
    private val senderDao: SenderDao
) {
    val allSenders: Flow<List<Sender>> = senderDao.getAllSenders()

    suspend fun insertSender(sender: Sender) {
        senderDao.insert(sender)
    }

    suspend fun updateSender(sender: Sender) {
        senderDao.update(sender)
    }

    suspend fun deleteSender(id: Long) {
        senderDao.deleteById(id)
    }

    suspend fun getSenderById(id: Long): Sender? {
        return senderDao.getSenderById(id).first()
    }
}
