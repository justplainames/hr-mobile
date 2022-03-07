package edu.singaporetech.hr.leave

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R

class LeaveRecordViewAllAdaptor(private val leaveRecordViewAllList: List<LeaveRecordViewAllItem>): RecyclerView.Adapter<LeaveRecordViewAllAdaptor.LeaveRecordViewAllViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRecordViewAllViewHolder {
        val itemViewViewAll = LayoutInflater.from(parent.context).inflate(
            R.layout.leave_view_all_record,
            parent, false)

        return LeaveRecordViewAllViewHolder(itemViewViewAll)
    }

    override fun onBindViewHolder(holder: LeaveRecordViewAllViewHolder, position: Int) {
        var currentItemViewAll = leaveRecordViewAllList[position]

        holder.selected.isChecked = currentItemViewAll.selected
        holder.leaveRecordViewAllType.text = currentItemViewAll.leaveRecordViewAllType
        holder.leaveRecordViewAllStartDate.text = currentItemViewAll.leaveRecordViewAllStartDate
        holder.leaveRecordViewAllEndDate.text = currentItemViewAll.leaveRecordViewAllEndDate
        holder.leaveRecordViewAllDays.text = currentItemViewAll.leaveRecordViewAllDays
        holder.leaveRecordViewAllStatus.text = currentItemViewAll.leaveRecordViewAllStatus

        holder.selected.setOnClickListener {
            if(currentItemViewAll.selected ==true){
                holder.selected.toggle()
                currentItemViewAll.selected = false
                holder.selected.setChecked(currentItemViewAll.selected)
            }else{
                holder.selected.toggle()
                currentItemViewAll.selected=true
                holder.selected.setChecked(currentItemViewAll.selected)
            }
        }
    }

    override fun getItemCount() = leaveRecordViewAllList.size

    class LeaveRecordViewAllViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val selected = itemView.findViewById<CheckedTextView>(R.id.checkedTextViewLeaveViewAllRecord)
        val leaveRecordViewAllType = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllType)
        val leaveRecordViewAllStartDate= itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllStartDate)
        val leaveRecordViewAllEndDate = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllEndDate)
        val leaveRecordViewAllDays = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllDays)
        val leaveRecordViewAllStatus = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllStatus)
    }
}