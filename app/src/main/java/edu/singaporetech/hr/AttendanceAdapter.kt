package edu.singaporetech.hr

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.leave.LeaveRecordViewAllAdaptor
import java.util.ArrayList

class AttendanceAdapter (private var attendanceArrayList: ArrayList<Attendance>,private val listener: AttendanceAdapter.OnItemClickListener) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {
    private lateinit var mListener : OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_attendenceitem, parent, false)
        )
    }


    interface OnItemClickListener {

        fun onItemClick(position: Int, clockInDate: String, id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }


    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

        var curItem = attendanceArrayList.get(position)
        val delim = " at "
        var tempDateTime = curItem.ClockInDate.toString()
        val test = tempDateTime.split(delim).last()
        Log.d("attendance", "temp: " + test)

        holder.dateTv.text = "${curItem.ClockInDate.toString()}"
        holder.timeTv.text = "${curItem.ClockOutDate.toString()}"
        holder.statusTV.text = "${curItem.attendanceStatus.toString()}"
    //    holder.timeTv.text = "${curItem.ClockOut.toString()}"//"${android.text.format.DateFormat.format("MMM yyyy", curItem.dateOfPayDay).toString()}"
    //    holder.statusTV.text = "${curItem.attendanceStatus}"

    }


    override fun getItemCount(): Int {
        return attendanceArrayList.size
    }
    inner class AttendanceViewHolder(views: View) : RecyclerView.ViewHolder(views), View.OnClickListener {
        var dateTv: TextView = views.findViewById(R.id.dateTv)
        var timeTv: TextView = views.findViewById(R.id.timeTv)
        var statusTV: TextView = views.findViewById(R.id.statusTV)
        var reportBtn: ImageButton = views.findViewById(R.id.reportBtn)
       // var attendanceSummaryBtn: Button = views.findViewById(R.id.attendanceSummaryBtn)

        init{
            reportBtn.setOnClickListener(this)

        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (v != null) {
                if(v.id==R.id.reportBtn){
                    if (position!=RecyclerView.NO_POSITION){
                        var curItem = attendanceArrayList.get(position)
                        var selectedDate =curItem.ClockInDate.toString()
                        var id = curItem.id.toString()
                           // clockInDate: String, id: String
                        listener.onItemClick(position,selectedDate,id)
                    }
                }

            }

        }


       // var nextButton: ImageButton = views.findViewById(R.id.nextButton)
     //   attendanceSummaryBtn.setOnClickListener(this);


    }

//    public void onClick(View view) {
//        int id = view.getId();
//        switch (id){
//            case R.id.object_card_first_button:
//            // button event
//            break;
//            case R.id.object_card_second_button
//                    // button event
//                    break;
//
//        }



}