package com.egadwys.gi_employee.payroll

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataViewHolder_payroll(view: View) : RecyclerView.ViewHolder(view) {
    val periode: TextView = view.findViewById(R.id.periode)

    fun bind(data: DataClass_payroll, clickListener: DataAdapter_payroll.OnItemClickListener) {
        periode.text = data.periode

        itemView.setOnClickListener {
            clickListener.onItemClick(data)
        }
    }
}
