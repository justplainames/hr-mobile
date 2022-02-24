package edu.singaporetech.hr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R

class PayslipDetailViewHolder(views: View) : RecyclerView.ViewHolder(views) {
    var bonus: TextView = views.findViewById(R.id.bonus)
    var totalEarning: TextView = views.findViewById(R.id.totalEarning)
    var ot: TextView = views.findViewById(R.id.ot)
    var basicWage: TextView = views.findViewById(R.id.basicWage)
}

class PayslipEarningDetailAdapter : RecyclerView.Adapter<PayslipDetailViewHolder>() {
    private var payslipList = listOf<Payslip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayslipDetailViewHolder {
        return PayslipDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_payslipdetailearning, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayslipDetailViewHolder, position: Int) {
        var curItem = payslipList.get(position)
        holder.bonus.text = "$ ${curItem.bonus}"
        holder.totalEarning.text = "$ ${curItem.totalEarning}"
        holder.basicWage.text = "$ ${curItem.basicWage}"
        holder.ot.text = "$ ${curItem.ot}"
    }

    override fun getItemCount(): Int {
        return payslipList.size
    }

    fun setDigitData(payslip: List<Payslip>) {
        this.payslipList = payslip
        notifyDataSetChanged()
    }

}
