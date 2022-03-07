package edu.singaporetech.hr

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

interface LeaveData {

    fun listLeave():ArrayList<Leave>
    fun addLeaveDetail(us:Leave)
    fun updateInfo(us:Leave)
    fun deleteInfo(id:Int)

    companion object{
        const val DATABASE_NAME =  "Hr"
        const val DATABASE_VERSION =1
        const val TABLE_NAME = "leaveDetails"
        const val COLUMN_ID = "leaveId"
        const val COL_LEAVE_TYPE = "leaveType"
        const val COL_LEAVE_STARTDATE = "leaveStartDate"
        const val COL_LEAVE_ENDDATE = "leaveEndDate"
        const val COL_LEAVE_DAY = "leaveDay"
        const val COL_LEAVE_SUPERVISOR = "leaveSupervisor"
        const val COL_LEAVE_REASON = "leaveReason"

        // SELECT DATA
        const val LEAVE_SELECT = "SELECT * FROM $TABLE_NAME"

        // CREATE TABLE
        const val TABLE_GET = "DROP TABLE EXISTS $TABLE_NAME"

        // SET TABLE
        const val TABLE_SET = "(CREATE TABLE $TABLE_NAME" +
                "($COLUMN_ID TEXT PRIMARY KEY,$COL_LEAVE_TYPE TEXT," +
                "$COL_LEAVE_STARTDATE TEXT,$COL_LEAVE_ENDDATE TEXT," +
                "$COL_LEAVE_DAY TEXT,$COL_LEAVE_SUPERVISOR TEXT,$COL_LEAVE_REASON TEXT)"


    }

}