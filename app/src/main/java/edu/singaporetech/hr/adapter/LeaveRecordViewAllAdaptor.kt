package edu.singaporetech.hr.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R
import edu.singaporetech.hr.data.LeaveRecordViewAllItem

/*
    LeaveRecordViewAllAdapter : Leave Record View All Adapter
        -- Used for recycler view by binding the data
           obtained from the database into the recycler view
           - Leave type, start date, end date, days and status
        -- Checkbox and Buttons embedded within the recycler view
               - with the use of onitemclicklistener, able to know
               whether it is click for selection or click next for leave details
 */

class LeaveRecordViewAllAdaptor(private val leaveRecordViewAllList: List<LeaveRecordViewAllItem>): RecyclerView.Adapter<LeaveRecordViewAllAdaptor.LeaveRecordViewAllViewHolder>(){
    private lateinit var mListener: onItemClickListener
    var selectedCheckBoxList : ArrayList<LeaveRecordViewAllItem> = ArrayList()

    interface onItemClickListener{
        fun onItemClickDetail(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveRecordViewAllViewHolder {
        val itemViewViewAll = LayoutInflater.from(parent.context).inflate(
            R.layout.leave_view_all_record,
            parent, false)

        return LeaveRecordViewAllViewHolder(itemViewViewAll, mListener)
    }

    override fun onBindViewHolder(holder: LeaveRecordViewAllViewHolder, position: Int) {

        var currentItemViewAll = leaveRecordViewAllList[position]

        holder.selected.isChecked = currentItemViewAll.selected
        holder.leaveRecordViewAllType.text = currentItemViewAll.leaveType
        holder.leaveRecordViewAllStartDate.text = currentItemViewAll.leaveStartDate+ " - " + currentItemViewAll.leaveEndDate
        holder.leaveRecordViewAllDays.text = currentItemViewAll.leaveDay
        holder.leaveRecordViewAllStatus.text = currentItemViewAll.leaveStatus

        holder.selected.setOnClickListener {
            if(currentItemViewAll.selected ==true){
                holder.selected.toggle()
                currentItemViewAll.selected = false
                holder.selected.setChecked(currentItemViewAll.selected)
                selectedCheckBoxList.remove(currentItemViewAll)

            }else{
                holder.selected.toggle()
                currentItemViewAll.selected=true
                holder.selected.setChecked(currentItemViewAll.selected)
                selectedCheckBoxList.add(currentItemViewAll)
            }
        }

    }

    override fun getItemCount() = leaveRecordViewAllList.size

    class LeaveRecordViewAllViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val selected = itemView.findViewById<CheckedTextView>(R.id.checkedTextViewLeaveViewAllRecord)
        val leaveRecordViewAllType = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllType)
        val leaveRecordViewAllStartDate= itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllStartDate)
        val leaveRecordViewAllDays = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllDay)
        val leaveRecordViewAllStatus = itemView.findViewById<TextView>(R.id.textViewLeaveRecordViewAllStatus)
        val leaveDetail = itemView.findViewById<ImageButton>(R.id.buttonLeaveDetail)

        init{
            leaveDetail.setOnClickListener {
                listener.onItemClickDetail(bindingAdapterPosition)
            }
        }
    }
}