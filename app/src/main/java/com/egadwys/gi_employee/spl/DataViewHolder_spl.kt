package com.egadwys.gi_employee.spl

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataViewHolder_spl(view: View) : RecyclerView.ViewHolder(view) {
    val idl: TextView = view.findViewById(R.id.idlspl)
    val spl: TextView = view.findViewById(R.id.splspl)
    val date: TextView = view.findViewById(R.id.datespl)
    val start: TextView = view.findViewById(R.id.startspl)
    val end: TextView = view.findViewById(R.id.endspl)
    val status: TextView = view.findViewById(R.id.statusspl)
    val remark: TextView = view.findViewById(R.id.remarkspl)
    val cardstatusspl: CardView = view.findViewById(R.id.cardstatusspl)

    @SuppressLint("SetTextI18n")
    fun bind(datax: DataClass_spl, clickListener: Spl) {
        idl.text = "#${datax.idL}"
        spl.text = datax.SPL
        date.text = datax.date
        start.text = datax.start
        end.text = datax.end
        status.text = datax.status
        remark.text = capitalizeEachWord(datax.remark)

        if (datax.status == "DITERIMA") {
            cardstatusspl.setCardBackgroundColor(Color.parseColor("#4CAF50"))
            status.setTextColor(Color.parseColor("#FFEEEEEE"))
        } else if (datax.status == "MENUNGGU") {
            cardstatusspl.setCardBackgroundColor(Color.parseColor("#FFEB3B"))
            status.setTextColor(Color.parseColor("#FF000000"))
        } else {
            cardstatusspl.setCardBackgroundColor(Color.parseColor("#F44336"))
        }

        itemView.setOnClickListener {
            clickListener.onItemClick(datax)
        }
    }

    fun capitalizeEachWord(input: String): String {
        return input.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    fun lowerchar(input: String): String {
        return input.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }
}
