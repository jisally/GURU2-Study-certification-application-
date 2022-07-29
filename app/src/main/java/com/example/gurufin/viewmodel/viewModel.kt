package com.example.gurufin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gurufin.dto.Target
import com.example.gurufin.repository.TargetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TargetViewModel: ViewModel() {
    val targetList: LiveData<MutableList<Target>>
    private val targetRepository: TargetRepository = TargetRepository.get()

    init {
        targetList = targetRepository.list()
    }

    fun getOne(num: Int) = targetRepository.getTarget(num)

    fun insert(dto: Target) = viewModelScope.launch(Dispatchers.IO) {
        targetRepository.insert(dto)
    }

    fun update(dto: Target) = viewModelScope.launch(Dispatchers.IO) {
        targetRepository.update(dto)
    }

    fun delete(dto: Target) = viewModelScope.launch(Dispatchers.IO) {
        targetRepository.delete(dto)
    }
}