package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import edu.singaporetech.hr.R

class ReportAdapter(var selectedDate: String, var id: String) {
//private var attendanceArrayList: ArrayList<Attendance>, private var position: Int
     fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.report_attendance_fragment,
            parent, false)

        return ReportAdapterViewHolder(itemView)
    }

    class ReportAdapterViewHolder(itemView: View) {
        val selectedDateTV = itemView.findViewById<TextView>(R.id.selectedDateTV)


    }





//    override fun getItemCount(): Int {
//
//        return attendanceArrayList.size
//    }


}