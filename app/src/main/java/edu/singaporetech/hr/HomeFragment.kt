package edu.singaporetech.hr

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {
        // TODO: Rename and change types of parameters



        private lateinit var viewModel: PayslipViewModel
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            val payslipButton= view.findViewById<Button>(R.id.payslipButton)
            val leaveButton = view.findViewById<Button>(R.id.leaveButton)

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)

            //COUNTDOWN
            viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
                val now= LocalDate.now()
                print(now)
                var payslipLatestDate: String =android.text.format.DateFormat.format("yyyy-MM-dd", payslip[0].dateOfPayDay).toString()
                var getDate = LocalDate.parse(payslipLatestDate.toString(), formatter)
                var currentDate = LocalDate.parse(now.toString(), formatter)
                var payDay = getDate.plusDays(30)
                var periodBtw=Period.between(currentDate,payDay)
                val info_text_payday_countdown = view.findViewById<TextView>(R.id.info_text_payday_countdown)
                if (periodBtw.toString().contains("D", ignoreCase = true))
                    info_text_payday_countdown.text= "${periodBtw.toString().drop(1)}AY\n TO\n PAY DAY"
                if (periodBtw.toString().contains("M", ignoreCase = true))
                    info_text_payday_countdown.text= "${periodBtw.toString().drop(1)}ONTH\n TO\n PAY DAY"
                createNotificationChannel()

                if (currentDate==payDay){
                    scheduleNotification()
                }
            })



            payslipButton.setOnClickListener {

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()

            }
            leaveButton.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, LeaveFragment())
                    .commitNow()
            }
            return view

        }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name="HR Notification Channel"
        val desc="desc"
        val channelId="channel1"
        val importance=NotificationManager.IMPORTANCE_DEFAULT
        val channel=NotificationChannel(channelId,name,importance)
        channel.description=desc
        val notificationManager= context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    private fun scheduleNotification() {

        val channelId="channel1"
        val notificationId=1
        val intent=Intent(requireActivity(),HomeFragment::class.java).apply { flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        val pendingIntent:PendingIntent=PendingIntent.getActivity(requireActivity(),0,intent,0)
        val notification= context?.let { NotificationCompat.Builder(it,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("HR")
            .setContentText("Latest Payslip is available for download!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build()
        }
        val manager= context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId,notification)
    }
}
