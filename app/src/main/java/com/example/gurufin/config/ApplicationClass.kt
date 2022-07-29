package com.example.gurufin.config

import android.app.Application
import com.example.gurufin.repository.TargetRepository

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        TargetRepository.initialize(this)
    }
}