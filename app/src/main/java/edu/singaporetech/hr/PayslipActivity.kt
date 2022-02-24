package edu.singaporetech.hr

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.Payslip
import edu.singaporetech.hr.PayslipAdapter
import edu.singaporetech.hr.PayslipViewModel

class PayslipActivity : AppCompatActivity() {
    private lateinit var viewModel: PayslipViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payslip)
        viewModel = ViewModelProvider(this).get(PayslipViewModel::class.java)
        val adapter = PayslipAdapter()
        //viewModel= ViewModelProvider(this).get(PayslipViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewPaySlip)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val digitListObserver = Observer<List<Payslip>> { payslip ->
            adapter.setDigitData(payslip)
        }
        viewModel = ViewModelProvider(this).get(PayslipViewModel::class.java)
        viewModel.getLatest3.observe(this, digitListObserver)

        val payslipListButton = findViewById<Button>(R.id.payslipListButton)
        payslipListButton.setOnClickListener {
            val intent = Intent(this, PayslipListActivity::class.java)
            startActivity(intent)
        }

        val payslipMoreDetailsButton = findViewById<ImageButton>(R.id.payslipMoreDetailsButton)
        payslipMoreDetailsButton.setOnClickListener {
            val intent = Intent(this, PayslipDetailActivity::class.java)

            startActivity(intent)

        }

        val payslipDownloadConsoButton = findViewById<Button>(R.id.payslipDownloadConsoButton)
        payslipDownloadConsoButton.setOnClickListener {
            val intent = Intent(this, ConsolidatedPaySlipActivity::class.java)
            startActivity(intent)
        }

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