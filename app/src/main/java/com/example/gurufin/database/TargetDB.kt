package com.example.gurufin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gurufin.dto.Target
import com.example.gurufin.dao.TargetDao

@Database(entities = arrayOf(Target::class), version=1)
abstract class TargetDB: RoomDatabase() {
    abstract fun TargetDao(): TargetDao
}