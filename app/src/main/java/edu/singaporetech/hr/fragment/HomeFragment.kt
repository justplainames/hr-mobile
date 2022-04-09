package edu.singaporetech.hr.fragment

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.AttendanceClockViewModel
import edu.singaporetech.hr.ViewModel.AttendanceViewModel
import edu.singaporetech.hr.ViewModel.LeaveViewModel
import edu.singaporetech.hr.ViewModel.PayslipViewModel
import edu.singaporetech.hr.databinding.FragmentHomeBinding
import edu.singaporetech.hr.fragment.attendance.AttendanceClockFragment
import edu.singaporetech.hr.fragment.attendance.AttendanceOverviewFragment
import edu.singaporetech.hr.fragment.leave.LeaveFragment
import edu.singaporetech.hr.fragment.payslip.PayslipFragment
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var viewLeaveModel: LeaveViewModel
    private lateinit var viewClockModel: AttendanceClockViewModel
    private lateinit var viewAttendanceViewModel: AttendanceViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater, R.layout.fragment_home, container, false
        )
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        viewModel = ViewModelProvider(requireActivity())[PayslipViewModel::class.java]
        viewLeaveModel = ViewModelProvider(requireActivity())[LeaveViewModel::class.java]
        viewClockModel = ViewModelProvider(requireActivity())[AttendanceClockViewModel::class.java]
        viewAttendanceViewModel =
            ViewModelProvider(requireActivity())[AttendanceViewModel::class.java]

        /**
         * Observe for any changes in the database to update attendance summary
         * 1) calculates number of total days worked
         * 2) calculates number of days missed
         * 3) calculates the number of hours worked
         * 4) calculates the total number of OT accumulated(>44hours/week)
         */

        viewClockModel.attendanceSummary.observe(viewLifecycleOwner) { summary ->

            // Updates number of days missed
            binding.tvAttendanceSummaryWorkingDaysValue.text = "${summary.daysWorked}\n Days"
            binding.tvAttendanceSummaryMissedDaysValue.text = "${summary.daysMissed}\n Days"
            when {
                summary.daysMissed in 6..8 -> {
                    binding.tvAttendanceSummaryMissedDaysValue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.orange
                        )
                    )
                }
                summary.daysMissed > 8 -> {
                    binding.tvAttendanceSummaryMissedDaysValue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.red
                        )
                    )
                }
                else -> {
                    binding.tvAttendanceSummaryMissedDaysValue.setTextColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.main
                        )
                    )
                }
            }

            // Updates the number of OT hours
            binding.tvAttendanceSummaryOTWorkedValue.text = "${summary.totalOT}\nHours "
            if (summary.totalOT > 40) {
                binding.tvAttendanceSummaryOTWorkedValue.setTextColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.red
                    )
                )
            }

            // Updates the number of hours worked
            binding.tvAttendanceSummaryHoursWorkedValue.text =
                "${summary.hoursWorked}:${summary.minutesWorked} \nHours"
            val formatValue = String.format("%.0f", summary.percentageMissed * 100)
            binding.infoTextAttendanceRate.text = "Attendance Rate \n $formatValue%"
            binding.circularAttendanceRate.apply {
                setProgressWithAnimation(summary.percentageMissed * 360f, 2000) // =1s
                progressMax = 360f
            }
        }

        /**
         * Observe for any changes in the database to update whether a user has clocked in or not
         */
        viewClockModel.attendance.observe(viewLifecycleOwner) { newTime ->
            val timeStamp: String
            if (newTime[0].clockOutDate == null) {
                binding.cardViewAttendanceCheck.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.green
                    )
                )
                timeStamp = newTime[0].clockInDate.toString()
                binding.ivCheck.visibility = View.VISIBLE
                binding.ivCross.visibility = View.INVISIBLE
                binding.infoTextTitleAttendance.text = timeStamp
            } else {
                binding.cardViewAttendanceCheck.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(), R.color.red
                    )
                )
                binding.ivCross.visibility = View.VISIBLE
                binding.ivCheck.visibility = View.INVISIBLE
                timeStamp = newTime[0].clockOutDate.toString()
                binding.infoTextTitleAttendance.text = timeStamp
            }
        }

        /**
         * Observe for any changes in the database to update the circular progress bar
         * Displays the amount of leave left over the total amount of leave in %
         */
        var totalLeave: Float
        var totalBalanceLeave: Float
        viewLeaveModel.leaveType.observe(viewLifecycleOwner) { leaveTypes ->
            totalLeave = leaveTypes[0].annualLeaveTotal.toFloat()
            totalBalanceLeave = leaveTypes[0].annualLeaveBalance.toFloat()

            totalLeave += leaveTypes[0].sickLeaveTotal.toInt()
            totalBalanceLeave += leaveTypes[0].sickLeaveBalance.toFloat()

            totalLeave += leaveTypes[0].maternityLeaveTotal.toFloat()
            totalBalanceLeave += leaveTypes[0].maternityLeaveBalance.toFloat()
            val value = totalBalanceLeave / totalLeave
            val formatValue = String.format("%.0f", value * 100)
            binding.infoTextRemainingLeave.text = "Remaining Leave:\n${formatValue}%"
            binding.circularProgressBar.apply {

                setProgressWithAnimation(value * 360f, 2000) // =1s
                progressMax = 360f
            }
        }

        /**
         * Observe for any changes in the database to update the number of days to payday
         */
        viewModel.payslip.observe(viewLifecycleOwner) { payslip ->
            val now = LocalDate.now()
            print(now)
            val payslipLatestDate: String =
                android.text.format.DateFormat.format("yyyy-MM-dd", payslip[0].dateOfPayDay)
                    .toString()
            val getDate = LocalDate.parse(payslipLatestDate, formatter)
            val currentDate = LocalDate.parse(now.toString(), formatter)
            val payDay = getDate.plusDays(30)
            val periodBtw = Period.between(currentDate, payDay)
            val infoTextPaydayCountdown =
                view?.findViewById<TextView>(R.id.info_text_payday_countdown)
            if (periodBtw.toString().contains("D", ignoreCase = true))
                infoTextPaydayCountdown?.text =
                    "${periodBtw.toString().drop(1)}AY\n TO\n PAY DAY"
            if (periodBtw.toString().contains("M", ignoreCase = true))
                infoTextPaydayCountdown?.text =
                    "${periodBtw.toString().drop(1)}MONTH\n TO\n PAY DAY"
            createNotificationChannel()

            if (currentDate == payDay) {
                scheduleNotification()
            }
        }
        /**
         * Hides the icons for check in
         */
        binding.ivCheck.visibility = View.INVISIBLE
        binding.ivCross.visibility = View.INVISIBLE

        /**
         * Binding for card view navigation
         */
        binding.cardPaydayCountdown.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipFragment())
                .commitNow()
        }
        binding.cardRemainingLeave.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, LeaveFragment())
                .commitNow()
        }
        binding.cardViewAttendance.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                .commitNow()
        }
        binding.cardViewAttendanceCheck.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceClockFragment())
                .commitNow()
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "HR Notification Channel"
        val desc = "desc"
        val channelId = "channel1"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        channel.description = desc
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleNotification() {
        val channelId = "channel1"
        val notificationId = 1
        val intent = Intent(requireActivity(), HomeFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireActivity(), 0, intent, 0)
        val notification = context?.let {
            NotificationCompat.Builder(it, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("HR")
                .setContentText("Latest Payslip is available for download!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build()
        }
        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

}
