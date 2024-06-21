package com.egadwys.gi_employee.cuti

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataViewHolder_cuti(view: View) : RecyclerView.ViewHolder(view) {
    val idl: TextView = view.findViewById(R.id.idcuti)
    val start: TextView = view.findViewById(R.id.startcuti)
    val end: TextView = view.findViewById(R.id.endcuti)
    val remark: TextView = view.findViewById(R.id.remarkcuti)

    @SuppressLint("SetTextI18n")
    fun bind(datax: DataClass_cuti, clickListener: Cuti) {
        idl.text = "#${datax.id}"
        start.text = datax.dateStart
        end.text = datax.dateEnd
        remark.text = datax.paidLeave

        itemView.setOnClickListener {
            clickListener.onItemClick(datax)
        }
    }
}
