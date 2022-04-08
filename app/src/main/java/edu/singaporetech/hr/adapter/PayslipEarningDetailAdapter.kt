package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.data.Payslip
import edu.singaporetech.hr.R

/*
    PayslipEarningDetailAdapter : Payslip Earning Detail Adapter
        -- Used for recycler view by binding the data
           obtained from the database into the recycler view
        -- This viewmodel is purely for the recycler view that display breakdown of earning
                - Bonus
                - Total Earning
                - Basic Wage
                - OT

 */
class PayslipEarningDetailAdapter (private var payslipArrayList: ArrayList<Payslip>,private var position: Int): RecyclerView.Adapter<PayslipEarningDetailAdapter.PayslipDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayslipDetailViewHolder {
        return PayslipDetailViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_payslipdetailearning, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PayslipDetailViewHolder, positionFetched: Int) {
        var curItem = payslipArrayList.get(position)
        holder.bonus.text = "$ ${curItem.bonus}"
        holder.totalEarning.text = "$ ${curItem.totalEarning}"
        holder.basicWage.text = "$ ${curItem.basicWage}"
        holder.ot.text = "$ ${curItem.ot}"
    }

    override fun getItemCount(): Int {
        //record return 1 record
        return 1
    }

    class PayslipDetailViewHolder(views: View) : RecyclerView.ViewHolder(views) {
        var bonus: TextView = views.findViewById(R.id.bonus)
        var totalEarning: TextView = views.findViewById(R.id.totalEarning)
        var ot: TextView = views.findViewById(R.id.ot)
        var basicWage: TextView = views.findViewById(R.id.basicWage)
    }

}
