package edu.singaporetech.hr.leave

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R

class LeaveRecordViewAll : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leave_all_record)

        val leaveRecordViewAllList = generateDummyDataList(10)

        val recyclerViewLeaveRecordViewAll: RecyclerView = findViewById(R.id.recyclerViewLeaveRecordViewAll)
        recyclerViewLeaveRecordViewAll.adapter = LeaveRecordViewAllAdaptor(leaveRecordViewAllList )
        recyclerViewLeaveRecordViewAll.layoutManager = LinearLayoutManager(this)



    }

    private fun generateDummyDataList(size:Int): List<LeaveRecordViewAllItem>{

        val list = ArrayList<LeaveRecordViewAllItem>()
        for (i in 0 until size){
            val item = LeaveRecordViewAllItem(false,"Annual", "$i","21/2","2.0","Accepted")
            list +=item
        }
        return list
    }
}