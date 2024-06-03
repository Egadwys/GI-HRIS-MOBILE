package com.egadwys.gi_employee

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class YourDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val hari: TextView = view.findViewById(R.id.hari)
    val tanggal: TextView = view.findViewById(R.id.tanggal)
    val tin: TextView = view.findViewById(R.id.tin)
    val tout: TextView = view.findViewById(R.id.tout)

    fun bind(datax: YourDataClass, clickListener: YourDataAdapter.OnItemClickListener) {
        hari.text = datax.hari
        tanggal.text = datax.tanggal
        tin.text = datax.masuk
        tout.text = datax.pulang

        itemView.setOnClickListener {
            clickListener.onItemClick(datax)
        }
    }
}
