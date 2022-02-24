package edu.singaporetech.hr

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Payslip::class], version = 1, exportSchema = false)
abstract class PayslipRoomDatabase : RoomDatabase() {
    abstract fun payslip(): PayslipDao

    companion object {
        @Volatile
        private var INSTANCE: PayslipRoomDatabase? = null

        fun getDatabase(context: Context): PayslipRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PayslipRoomDatabase::class.java,
                    "Payslip"
                ).build()
                INSTANCE = instance
                // return instance
                return instance
            }
        }
    }
}


