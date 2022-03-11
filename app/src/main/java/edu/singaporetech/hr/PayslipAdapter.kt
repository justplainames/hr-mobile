package edu.singaporetech.hr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R
import java.util.*
import kotlin.collections.ArrayList


class PayslipAdapter(private var payslipArrayList: ArrayList<Payslip>) : RecyclerView.Adapter<PayslipAdapter.PayslipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayslipViewHolder {
        return PayslipViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_payslipitem, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayslipViewHolder, position: Int) {

        var curItem = payslipArrayList.get(position)

        holder.netPay.text = "$ ${curItem.netPay}"
        holder.payslipMthYear.text = "${android.text.format.DateFormat.format("MMM yyyy", curItem.dateOfPayDay).toString()}"
    }

    override fun getItemCount(): Int {

        return payslipArrayList.size
    }

    class PayslipViewHolder(views: View) : RecyclerView.ViewHolder(views) {
        var netPay: TextView = views.findViewById(R.id.netPay)
        var payslipMthYear: TextView = views.findViewById(R.id.payslipMthYear)

    }
}

