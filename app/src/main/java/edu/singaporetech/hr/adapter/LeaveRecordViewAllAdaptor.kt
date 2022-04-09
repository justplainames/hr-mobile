package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R
import edu.singaporetech.hr.data.LeaveRecordViewAllItem


class LeaveRecordViewAllAdaptor(private val leaveRecordViewAllList: List<LeaveRecordViewAllItem>): RecyclerView.Adapter<LeaveRecordViewAllAdaptor.LeaveRecordViewAllViewHolder>(){
    private lateinit var mListener: onItemClickListener
    var selectedCheckBoxList : ArrayList<LeaveRecordViewAllItem> = ArrayList()
    interface onItemClickListener{

        fun onItemClickDetail(position: Int)
//        fun onItemClickDetail(position: Int)
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
//            leaveDetail.setOnClickListener {
//                listener.onItemClickDetail(bindingAdapterPosition)
//            }
        }
    }
}