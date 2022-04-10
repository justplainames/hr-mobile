package edu.singaporetech.hr.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.Attendance
import edu.singaporetech.hr.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/*
    AttendanceAdapter : Attendance Adapter
        -- Used for recycler view by binding the data
           obtained from the database into the recycler view
           - Clock In, Out, Status and Reason
        -- Buttons embedded within the recycler view
               - with the use of onitemclicklistener, able to know
               whether it is click next for report of issues
 */
class AttendanceAdapter(
    private var attendanceArrayList: ArrayList<Attendance>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        return AttendanceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.attendence_item, parent, false)
        )
    }


    interface OnItemClickListener {

        fun onItemClick(position: Int, clockInDate: String, id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {

        val curItem = attendanceArrayList.get(position)
        val delim = " at "
        val tempDateTime = curItem.ClockInDate.toString()
        val test = tempDateTime.split(delim).last()
        Log.d("attendance", "temp: $test")
        var clockin: String? = ""
        clockin = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(curItem.ClockInDate)
        var clockout: String? = ""
        if (curItem.ClockOutDate != null) {
            clockout =
                SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH).format(curItem.ClockOutDate)
        }
        holder.dateTv.text = "ClockIn: ${clockin}"
        holder.timeTv.text = "ClockOut:${clockout}"
        if (curItem.attendanceStatus == null) {
            holder.statusTV.text = "Status:"
        } else {
            holder.statusTV.text = "Status: ${curItem.attendanceStatus.toString()}"
        }
        if (curItem.IssueReason == null) {
            holder.reason.text = "Reason:"
        } else {
            holder.reason.text = "Reason: ${curItem.IssueReason.toString()}"
        }


    }


    override fun getItemCount(): Int {
        return attendanceArrayList.size
    }

    inner class AttendanceViewHolder(views: View) : RecyclerView.ViewHolder(views),
        View.OnClickListener {
        var dateTv: TextView = views.findViewById(R.id.dateLbl)
        var timeTv: TextView = views.findViewById(R.id.timeLbl)
        var statusTV: TextView = views.findViewById(R.id.StatusLbl)
        var reportBtn: ImageButton = views.findViewById(R.id.reportBtn)
        var reason: TextView = views.findViewById(R.id.reason)
        // var attendanceSummaryBtn: Button = views.findViewById(R.id.attendanceSummaryBtn)

        init {
            reportBtn.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (v != null) {
                if (v.id == R.id.reportBtn) {
                    if (position != RecyclerView.NO_POSITION) {
                        val curItem = attendanceArrayList.get(position)
                        val selectedDate = curItem.ClockInDate.toString()
                        val id = curItem.id.toString()
                        // clockInDate: String, id: String
                        listener.onItemClick(position, selectedDate, id)
                    }
                }
            }
        }
    }
}