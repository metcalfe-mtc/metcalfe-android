package com.lang.ktn.bean.sql

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AccountDao {


    @Query("SELECT * FROM account_table")
    fun queryAllUserInfo(): List<SqlAddres>

    @Query("SELECT * FROM account_table WHERE id = :id")
    fun getUserById(id: Long): SqlAddres

    @Query("SELECT * FROM account_table WHERE address = :address")
    fun getUserById(address: String): SqlAddres

    @Query("SELECT * FROM account_table WHERE afault = :afault")
    fun getDefaultWallet(afault: String): SqlAddres

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg addres: SqlAddres): LongArray

    @Update
    fun updateUser(addres: SqlAddres)

    @Delete
    fun deleteUser(addres: SqlAddres)


}
