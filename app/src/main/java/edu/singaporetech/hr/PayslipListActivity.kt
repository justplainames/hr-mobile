package edu.singaporetech.hr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.firstapp.Payslip
import edu.singaporetech.firstapp.PayslipAdapter
import edu.singaporetech.firstapp.PayslipViewModel
import edu.singaporetech.hr.R


class PayslipListActivity: AppCompatActivity() {
    private lateinit var viewModel: PayslipViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paysliplist)
        val adapter= PayslipAdapter()
        val recyclerView=findViewById<RecyclerView>(R.id.payslipListRecyclerView)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this)
        val digitListObserver= Observer<List<Payslip>> { payslip ->
            adapter.setDigitData(payslip)
        }
        viewModel= ViewModelProvider(this).get(PayslipViewModel::class.java)
        viewModel.getAll.observe(this,digitListObserver)
    }

}
