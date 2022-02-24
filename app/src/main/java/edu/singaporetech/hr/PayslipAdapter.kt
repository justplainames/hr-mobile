package edu.singaporetech.firstapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.R

class PayslipViewHolder (views:View):RecyclerView.ViewHolder(views){
    var netPay:TextView=views.findViewById(R.id.netPay)
    var payslipMthYear:TextView=views.findViewById(R.id.payslipMthYear)
}

class PayslipAdapter: RecyclerView.Adapter<PayslipViewHolder>(){
    private var payslipList= listOf<Payslip>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayslipViewHolder{
        return PayslipViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_payslipitem, parent, false))
    }

    override fun onBindViewHolder(holder:PayslipViewHolder, position: Int) {
        var curItem=payslipList.get(position)
        holder.netPay.text= "$ ${curItem.netPay}"
        holder.payslipMthYear.text="${curItem.dateOfPayDay}"
    }
    override fun getItemCount(): Int {
        return payslipList.size
    }
    fun setDigitData(payslip:List<Payslip>){
        this.payslipList=payslip
        notifyDataSetChanged()
    }

}
