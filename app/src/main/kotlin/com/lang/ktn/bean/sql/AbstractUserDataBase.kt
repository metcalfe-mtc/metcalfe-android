package com.lang.ktn.bean.sql

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SqlAddres::class], version = 2)
abstract class AbstractUserDataBase : RoomDatabase() {
    abstract val accountDao: AccountDao
}