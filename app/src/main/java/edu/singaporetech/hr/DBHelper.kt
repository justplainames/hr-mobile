package edu.singaporetech.hr

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.singaporetech.hr.LeaveData.Companion.TABLE_NAME
import edu.singaporetech.hr.Leave
import kotlin.collections.ArrayList as ArrayList

class DBHelper(
    context: Context?,
  //  name: String?,
  //  factory: SQLiteDatabase.CursorFactory?,
  //  version: Int
): SQLiteOpenHelper (context, LeaveData.DATABASE_NAME, null,LeaveData.DATABASE_VERSION), LeaveData{

    lateinit var values:ContentValues
    lateinit var db:SQLiteDatabase
    var cursor: Cursor? = null

//    companion object{
//        private val version = 1
//        private val name =  "Hr.db"
//        val TABLE_NAME = "leaveDetails"
//        val COLUMN_ID = "_leaveid"
//        val COLUMN_NAME1 = "leaveType"
//        val COLUMN_NAME2 = "leaveStartDate"
//        val COLUMN_NAME3 = "leaveEndDate"
//        val COLUMN_NAME4 = "leaveDay"
//        val COLUMN_NAME5 = "leaveSupervisor"
//        val COLUMN_NAME6 = "leaveReason"
//    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(LeaveData.TABLE_SET)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(LeaveData.TABLE_GET)
        onCreate(db)
    }

//    fun addLeave(name: Leave){
//        val values = ContentValues()
//        values.put(COLUMN_NAME1, name.leaveType)
//        values.put(COLUMN_NAME2, name.leaveStartDate)
//        values.put(COLUMN_NAME3, name.leaveEndDate)
//        values.put(COLUMN_NAME4, name.leaveDay)
//        values.put(COLUMN_NAME5, name.leaveSupervisor)
//        values.put(COLUMN_NAME6, name.leaveReason)
//
//        val db = this.writableDatabase
//        db.insert(TABLE_NAME, null, values)
//        db.close()
//    }

    override fun listLeave(): ArrayList<Leave> {
        db = this.readableDatabase
        val storeLeaveInfo = ArrayList<Leave>()
        cursor = db.rawQuery(LeaveData.LEAVE_SELECT, null)
        if(cursor!!.moveToFirst()){
            do{
                val leaveId = cursor!!.getString(0)
                val leaveType = cursor!!.getString(1)
                val leaveStartDate = cursor!!.getString(2)
                val leaveEndDate = cursor!!.getString(3)
                val leaveDay = cursor!!.getString(4)
                val leaveSupervisor = cursor!!.getString(5)
                val leaveReason = cursor!!.getString(6)
                //storeLeaveInfo.add(Leave(leaveId, leaveType, leaveStartDate, leaveEndDate, leaveDay, leaveSupervisor, leaveReason))
            } while(cursor!!.moveToNext())
        }
        cursor!!.requery()
        cursor!!.close()
        return storeLeaveInfo


    }

    override fun addLeaveDetail(us: edu.singaporetech.hr.Leave) {
        values = ContentValues()
        cursor = db.rawQuery(LeaveData.LEAVE_SELECT, null)

        values.put(LeaveData.COL_LEAVE_TYPE, us.leaveType)
        values.put(LeaveData.COL_LEAVE_STARTDATE, us.leaveStartDate)
        values.put(LeaveData.COL_LEAVE_ENDDATE, us.leaveEndDate)
        values.put(LeaveData.COL_LEAVE_DAY, us.leaveDay)
        values.put(LeaveData.COL_LEAVE_SUPERVISOR, us.leaveSupervisor)
        values.put(LeaveData.COL_LEAVE_REASON, us.leaveReason)
        db.insert(LeaveData.TABLE_NAME, null, values)
        cursor!!.requery()




    }

    override fun updateInfo(us: edu.singaporetech.hr.Leave) {
        TODO("Not yet implemented")
    }

    override fun deleteInfo(id: Int) {
        db = this.writableDatabase
        db.delete(LeaveData.TABLE_NAME,
        "${LeaveData.COLUMN_ID}=?", arrayOf(id.toString()))
    }

//    fun getAllLeave(): Cursor?{
//        val db = this.readableDatabase
//        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
//    }

}