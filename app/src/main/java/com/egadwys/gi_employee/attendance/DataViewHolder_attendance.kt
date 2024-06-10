package com.egadwys.gi_employee.attendance

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataViewHolder_attendance(view: View) : RecyclerView.ViewHolder(view) {
    val hari: TextView = view.findViewById(R.id.hari)
    val tanggal: TextView = view.findViewById(R.id.tanggal)
    val tin: TextView = view.findViewById(R.id.tin)
    val tout: TextView = view.findViewById(R.id.tout)

    fun bind(datax: DataClass_attendance, clickListener: DataAdapter_attendance.OnItemClickListener) {
        hari.text = datax.hari
        tanggal.text = datax.tanggal
        tin.text = datax.masuk
        tout.text = datax.pulang

        itemView.setOnClickListener {
            clickListener.onItemClick(datax)
        }
    }
}
