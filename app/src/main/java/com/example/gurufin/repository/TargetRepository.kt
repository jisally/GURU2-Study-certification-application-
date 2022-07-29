package com.example.gurufin.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.gurufin.database.TargetDB
import com.example.gurufin.dto.Target

private const val DB_NAME = "target_db.db"

class TargetRepository private constructor(context: Context){
    private val database: TargetDB = Room.databaseBuilder(
        context.applicationContext,
        TargetDB::class.java,
        DB_NAME
    ). build()

    private val TargetDao=database.TargetDao()
    fun list(): LiveData<MutableList<Target>> = TargetDao.list()
    fun getTarget(num: Int): Target = TargetDao.selectOne(num)
    fun insert(dto: Target) = TargetDao.insert(dto)
    suspend fun update(dto: Target) = TargetDao.update(dto)
    fun delete(dto: Target) = TargetDao.delete(dto)

    companion object {
        private var INSTANCE : TargetRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE==null) {
                INSTANCE = TargetRepository(context)
            }
        }

        fun get(): TargetRepository {
            return INSTANCE ?:
            throw  IllegalStateException("TargetRepository must be initialized")
        }
    }
}