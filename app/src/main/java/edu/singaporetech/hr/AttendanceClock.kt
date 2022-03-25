package edu.singaporetech.hr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class AttendanceClock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_clock)

        val attendanceClockFragment = AttendanceClockFragment()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.attendclock_frame_layout, attendanceClockFragment)
            .commit()
    }

}