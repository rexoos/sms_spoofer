package com.example.smsspoof.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smsspoof.data.SenderRepository
import com.example.smsspoof.model.Sender
import kotlinx.coroutines.launch

class SenderViewModel(application: Application, private val senderRepository: SenderRepository) :
    AndroidViewModel(application) {

    val allSenders: LiveData<List<Sender>> = senderRepository.allSenders.asLiveData()

    fun insertSender(sender: Sender) = viewModelScope.launch {
        senderRepository.insertSender(sender)
    }

    fun updateSender(sender: Sender) = viewModelScope.launch {
        senderRepository.updateSender(sender)
    }

    fun deleteSender(id: Long) = viewModelScope.launch {
        senderRepository.deleteSender(id)
    }
}
