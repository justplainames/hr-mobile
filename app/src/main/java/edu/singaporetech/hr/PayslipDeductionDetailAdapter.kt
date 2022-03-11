package edu.singaporetech.hr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R


class PayslipDeductionDetailAdapter(private var payslipArrayList: ArrayList<Payslip>) : RecyclerView.Adapter<PayslipDeductionDetailAdapter.PayslipDeductionDetailViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PayslipDeductionDetailViewHolder {
        return PayslipDeductionDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_payslipdetaildeduction, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayslipDeductionDetailViewHolder, position: Int) {
        var curItem = payslipArrayList.get(position)
        holder.opeOthers.text = "$ ${curItem.opeOthers}"
        holder.cpf.text = "$ ${curItem.cpf}"
        holder.totalDeduction.text = "$ ${curItem.totalDeduction}"
        holder.asstFund.text = "$ ${curItem.asstFund}"

    }

    override fun getItemCount(): Int {
        return payslipArrayList.size
    }

    class PayslipDeductionDetailViewHolder(views: View) : RecyclerView.ViewHolder(views) {
        var opeOthers: TextView = views.findViewById(R.id.opeOthers)
        var cpf: TextView = views.findViewById(R.id.cpf)
        var totalDeduction: TextView = views.findViewById(R.id.totalDeduction)
        var asstFund: TextView = views.findViewById(R.id.asstFund)
    }

}
