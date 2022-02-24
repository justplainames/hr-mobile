package edu.singaporetech.hr


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Payslip")
data class Payslip(
    @PrimaryKey(autoGenerate = true)
    val payslip_ID: Int,
    @ColumnInfo(name = "totalDeduction")
    val totalDeduction: Double,
    @ColumnInfo(name = "cpf")
    val cpf: Double,
    @ColumnInfo(name = "asstFund")
    val asstFund: Double,
    @ColumnInfo(name = "totalEarning")
    val totalEarning: Double,
    @ColumnInfo(name = "basicWage")
    val basicWage: Double,
    @ColumnInfo(name = "bonus")
    val bonus: Double,
    @ColumnInfo(name = "opeOthers")
    val opeOthers: Double,
    @ColumnInfo(name = "dataOfPayDay")
    val dateOfPayDay: String,
    @ColumnInfo(name = "netPay")
    val netPay: Double,
    @ColumnInfo(name = "ot")
    val ot: Double,
    @ColumnInfo(name = "user_ID")
    val user_ID: Int
)



