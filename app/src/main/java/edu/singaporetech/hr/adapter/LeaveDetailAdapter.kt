package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.singaporetech.hr.R
import edu.singaporetech.hr.data.LeaveRecordViewAllItem

/*
    LeaveDetailAdapter : Leave Detail Adapter
        -- Used for recycler view by binding the data
           obtained from the database into the recycler view
           - Leave type, start date, end date, days, status, supervisor and documents uploaded
        --  This viewmodel is purely for the recycler view that display details of leave record
 */

class LeaveDetailAdapter(private val leaveRecordViewAllList: ArrayList<LeaveRecordViewAllItem>, private var position: Int): RecyclerView.Adapter<LeaveDetailAdapter.LeaveDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveDetailViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.leave_detail_item,
            parent, false)

        return LeaveDetailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LeaveDetailViewHolder, position_fetched: Int) {

        var currentItemViewAll = leaveRecordViewAllList[position]

        holder.leaveType.text = currentItemViewAll.leaveType
        holder.leaveRecordViewAllStartDate.text = currentItemViewAll.leaveStartDate+ " - " + currentItemViewAll.leaveEndDate
        holder.leaveRecordViewAllDays.text = currentItemViewAll.leaveDay
        holder.leaveRecordViewAllStatus.text = currentItemViewAll.leaveStatus
        holder.leaveRecordViewAllReason.text = currentItemViewAll.leaveReason
        holder.leaveRecordViewAllSupervisor.text = currentItemViewAll.leaveSupervisor

        Glide.with(holder.itemView).load(currentItemViewAll.imageRef).into(holder.imageViewLeave)

    }

    override fun getItemCount() = 1

    class LeaveDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val leaveType = itemView.findViewById<TextView>(R.id.textViewLeaveTypeDetail)
        val leaveRecordViewAllStartDate= itemView.findViewById<TextView>(R.id.textViewLeaveDateDetails)
        val leaveRecordViewAllDays = itemView.findViewById<TextView>(R.id.textViewLeaveDaysDetail)
        val leaveRecordViewAllSupervisor = itemView.findViewById<TextView>(R.id.textViewLeaveSupervisorDetail)
        val leaveRecordViewAllReason = itemView.findViewById<TextView>(R.id.textViewLeaveReasonDetail)
        val leaveRecordViewAllStatus = itemView.findViewById<TextView>(R.id.textViewLeaveStatusDetail)
        val imageViewLeave  = itemView.findViewById<ImageView>(R.id.imageViewLeave)
    }

}

