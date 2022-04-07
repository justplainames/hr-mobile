package edu.singaporetech.hr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.data.Payslip
import edu.singaporetech.hr.R
import java.util.*


class PayslipAdapter(private var payslipArrayList: ArrayList<Payslip>, private val listener: OnItemClickListener) : RecyclerView.Adapter<PayslipAdapter.PayslipViewHolder>() {

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

    inner class PayslipViewHolder(views: View) : RecyclerView.ViewHolder(views),View.OnClickListener {
        var netPay: TextView = views.findViewById(R.id.netPay)
        var payslipMthYear: TextView = views.findViewById(R.id.payslipMthYear)
        var downloadButton: ImageButton = views.findViewById(R.id.downloadButton)
        var nextButton: ImageButton = views.findViewById(R.id.nextButton)

        init{
            downloadButton.setOnClickListener(this)
            nextButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (v != null) {
                if(v.id== R.id.downloadButton){
                    if (position!=RecyclerView.NO_POSITION){
                        listener.onItemClickDownload(position)

                    }
                }else if (v.id== R.id.nextButton){
                    if (position!=RecyclerView.NO_POSITION){
                        listener.onItemClickNext(position)
                    }

                }

            }

        }
    }
    interface OnItemClickListener{
        fun onItemClickDownload(position: Int)
        fun onItemClickNext(position: Int)
    }

}

