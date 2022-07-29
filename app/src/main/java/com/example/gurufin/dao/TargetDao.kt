package com.example.gurufin.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.gurufin.dto.Target

@Dao
interface TargetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dto: Target)

    @Query("select * from targetTbl")
    fun list(): LiveData<MutableList<Target>>

    @Query("select * from targetTbl where num=(:num)")
    fun selectOne(num: Int): Target

    @Update
    suspend fun update(dto: Target)

    @Delete
    fun delete(dto: Target)


}