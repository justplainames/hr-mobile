package edu.singaporetech.hr.leave

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R

class LeaveMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leave_homepage)

        val buttonApplyLeave = findViewById<Button>(R.id.buttonApplyLeave)
        val buttonViewAllLeave = findViewById<Button>(R.id.buttonViewAllLeave)


        buttonApplyLeave.setOnClickListener {
            val applyLeaveIntent = Intent(this, LeaveApplyLeave::class.java)
            startActivity(applyLeaveIntent)
        }

        buttonViewAllLeave.setOnClickListener {
            val viewAllLeaveIntent = Intent(this, LeaveRecordViewAll::class.java)
            startActivity(viewAllLeaveIntent)
        }

        val progressBarAnnualLeave = findViewById<ProgressBar>(R.id.progressBarAnnualLeave)
        val progressBarSickLeave = findViewById<ProgressBar>(R.id.progressBarSickLeave)
        val progressBarMaternityLeave = findViewById<ProgressBar>(R.id.progressBarMaternityLeave)

        progressBarAnnualLeave.max = 10
        progressBarSickLeave.max = 10
        progressBarMaternityLeave.max = 10

        val currentProgressAnnualLeave = 4
        val currentProgressSickLeave = 6
        val currentProgressMaternityLeave = 7

        ObjectAnimator.ofInt(progressBarAnnualLeave,"progress",currentProgressAnnualLeave)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofInt(progressBarSickLeave,"progress",currentProgressSickLeave)
            .setDuration(1000)
            .start()

        ObjectAnimator.ofInt(progressBarMaternityLeave,"progress",currentProgressMaternityLeave)
            .setDuration(1000)
            .start()


//        val leaveRecordList = generateDummyDataList(10)
//
//        val recyclerViewLeaveRecord: RecyclerView = findViewById(R.id.recyclerViewLeaveRecord)
//        recyclerViewLeaveRecord.adapter = LeaveRecordAdaptor(leaveRecordList)
//        recyclerViewLeaveRecord.layoutManager = LinearLayoutManager(this)
//        recyclerViewLeaveRecord.setHasFixedSize(true)

    }

    private fun generateDummyDataList(size:Int): List<LeaveRecordItem>{

        val list = ArrayList<LeaveRecordItem>()
        for (i in 0 until size){
            val item = LeaveRecordItem("Annual", "$i","21/2","2.0","Accepted")
            list +=item
        }
        return list
    }
}