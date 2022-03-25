package edu.singaporetech.hr

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class AttendanceAdapter (private var attendanceArrayList: ArrayList<Attendance>) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_attendenceitem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

        var curItem = attendanceArrayList.get(position)
        val delim = " at "
        var tempDateTime = curItem.ClockInDate.toString()
        tempDateTime.split(delim)
        Log.d("attendance", "temp: " + tempDateTime)

        holder.dateTv.text = "${curItem.ClockInDate.toString()}"
        holder.timeTv.text = "${curItem.ClockInDate.toString()}"
        holder.statusTV.text = "${curItem.attendanceStatus.toString()}"
    //    holder.timeTv.text = "${curItem.ClockOut.toString()}"//"${android.text.format.DateFormat.format("MMM yyyy", curItem.dateOfPayDay).toString()}"
    //    holder.statusTV.text = "${curItem.attendanceStatus}"





    }

    override fun getItemCount(): Int {

        return attendanceArrayList.size
    }

    inner class AttendanceViewHolder(views: View) : RecyclerView.ViewHolder(views) {
        var dateTv: TextView = views.findViewById(R.id.dateTv)
        var timeTv: TextView = views.findViewById(R.id.timeTv)
        var statusTV: TextView = views.findViewById(R.id.statusTV)
     //   var reportButton: Button = views.findViewById(R.id.reportBtn)
      //  var attendanceSummaryBtn: Button = views.findViewById(R.id.attendanceSummaryBtn)
     //   var downloadButton: ImageButton = views.findViewById(R.id.downloadButton)
     //   var nextButton: ImageButton = views.findViewById(R.id.nextButton)

//        init{
//            downloadButton.setOnClickListener(this)
//            nextButton.setOnClickListener(this)
//        }

//        override fun onClick(v: View?) {
//            val position:Int=adapterPosition
//            if (v != null) {
//                if(v.id==R.id.downloadButton){
//                    if (position!= RecyclerView.NO_POSITION){
//                        listener.onItemClickDownload(position)
//
//                    }
//                }else if (v.id==R.id.nextButton){
//                    if (position!= RecyclerView.NO_POSITION){
//                        listener.onItemClickNext(position)
//                    }
//
//                }
//
//            }
//
//        }
    }
//    interface OnItemClickListener{
//        fun onItemClickDownload(position: Int)
//        fun onItemClickNext(position: Int)
//    }

}