package com.github.intershitty.app.database.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.github.intershitty.app.database.entities.AccountEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    suspend fun getAll(): List<AccountEntity>

    @Insert
    suspend fun insertAll(vararg accounts: AccountEntity)

    @Delete
    suspend fun delete(account: AccountEntity)

    @Update
    suspend fun update(vararg accounts: AccountEntity)
}