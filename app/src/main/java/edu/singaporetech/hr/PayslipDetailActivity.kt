package edu.singaporetech.hr

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.Payslip
import edu.singaporetech.hr.PayslipDeductionDetailAdapter
import edu.singaporetech.hr.PayslipEarningDetailAdapter
import edu.singaporetech.hr.PayslipViewModel

class PayslipDetailActivity : AppCompatActivity() {
    private lateinit var viewModel1: PayslipViewModel
    private lateinit var viewModel2: PayslipViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payslipdetail)
        val adapterEarning = PayslipEarningDetailAdapter()

        val recyclerViewEarning = findViewById<RecyclerView>(R.id.recyclerViewPaySlipDetailEarning)
        recyclerViewEarning.adapter = adapterEarning
        recyclerViewEarning.layoutManager = LinearLayoutManager(this)
        val digitListObserverEarning = Observer<List<Payslip>> { payslip ->
            adapterEarning.setDigitData(payslip)
        }
        viewModel2 = ViewModelProvider(this).get(PayslipViewModel::class.java)
        viewModel2.getLatestMth.observe(this, digitListObserverEarning)

        val adapterDeduction = PayslipDeductionDetailAdapter()
        val recyclerViewDeduction =
            findViewById<RecyclerView>(R.id.recyclerViewPaySlipDetailDeduction)
        recyclerViewDeduction.adapter = adapterDeduction
        recyclerViewDeduction.layoutManager = LinearLayoutManager(this)
        val digitListObserverDeduction = Observer<List<Payslip>> { payslip ->
            adapterDeduction.setDigitData(payslip)
        }
        viewModel1 = ViewModelProvider(this).get(PayslipViewModel::class.java)
        viewModel1.getLatestMth.observe(this, digitListObserverDeduction)
        viewModel1.getLatestMth.observe(this, digitListObserverDeduction)
        val circularProgressBar = findViewById<CircularProgressBar>(R.id.circularProgressBar)

        circularProgressBar.apply {
            setProgressWithAnimation(180f, 1000) // =1s
            // Set Progress Max
            progressMax = 360f
            // Set ProgressBar Color
            progressBarColor = Color.GREEN
            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.BLUE
            backgroundProgressBarColorEnd = Color.BLUE
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 7f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
    }

}