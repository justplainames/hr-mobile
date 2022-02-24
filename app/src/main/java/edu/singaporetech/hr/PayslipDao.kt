package edu.singaporetech.hr

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PayslipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(payslip: Payslip)

    @Query("DELETE FROM Payslip")
    suspend fun deleteAll()

    @Query("SELECT * FROM Payslip ORDER BY payslip_ID ASC LIMIT 3")
    fun getLatest3(): LiveData<List<Payslip>>

    @Query("SELECT * FROM Payslip ORDER BY payslip_ID ASC")
    fun getAll(): LiveData<List<Payslip>>

    @Query("SELECT * FROM Payslip ORDER BY payslip_ID ASC LIMIT 1")
    fun getLatestMth(): LiveData<List<Payslip>>
}

