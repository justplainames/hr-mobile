package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.data.Leave
import edu.singaporetech.hr.R

/*
    LeaveRecordAdapter : Leave Record Adapter
        -- Used for recycler view by binding the data
           obtained from the database into the recycler view
           - Leave type, start date, end date, days and status
 */

class LeaveRecordAdaptor(private val leaveRecordList: ArrayList<Leave>): RecyclerView.Adapter<LeaveRecordAdaptor.LeaveRecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRecordViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.leave_record,
            parent, false)

        return LeaveRecordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LeaveRecordViewHolder, position: Int) {
        val currentItem = leaveRecordList[position]

        holder.leaveRecordType.text = currentItem.leaveType
        holder.leaveRecordStartDate.text = currentItem.leaveStartDate + " - " + currentItem.leaveEndDate
        holder.leaveRecordDays.text = currentItem.leaveDay
        holder.leaveRecordStatus.text = currentItem.leaveStatus
    }

    override fun getItemCount() = leaveRecordList.size

    class LeaveRecordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val leaveRecordType= itemView.findViewById<TextView>(R.id.textViewLeaveRecordType)
        val leaveRecordStartDate = itemView.findViewById<TextView>(R.id.textViewLeaveRecordStartDate)
        val leaveRecordDays = itemView.findViewById<TextView>(R.id.textViewLeaveRecordDays)
        val leaveRecordStatus = itemView.findViewById<TextView>(R.id.textViewLeaveRecordStatus)
    }
}