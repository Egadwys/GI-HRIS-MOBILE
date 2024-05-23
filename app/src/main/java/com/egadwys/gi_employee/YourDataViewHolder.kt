package com.egadwys.gi_employee

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class YourDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val NIK: TextView = view.findViewById(R.id.nik)
    val name: TextView = view.findViewById(R.id.name)
    val position: TextView = view.findViewById(R.id.position)
    val dept: TextView = view.findViewById(R.id.dept)
    val tin: TextView = view.findViewById(R.id.tin)
    val tout: TextView = view.findViewById(R.id.tout)

    fun bind(datax: YourDataClass, clickListener: YourDataAdapter.OnItemClickListener) {
        name.text = datax.name
        NIK.text = datax.NIK.toString()
        position.text = datax.position
        dept.text = datax.dept
        tin.text = datax.masuk
        tout.text = datax.pulang

        itemView.setOnClickListener {
            clickListener.onItemClick(datax)
        }
    }
}
