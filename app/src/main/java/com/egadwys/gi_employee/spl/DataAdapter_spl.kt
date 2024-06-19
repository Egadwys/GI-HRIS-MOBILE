package com.egadwys.gi_employee.spl

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataAdapter_spl(
    private var dataList: List<DataClass_spl>,
    private val itemClickListener: Spl
) : RecyclerView.Adapter<DataViewHolder_spl>(), Filterable {

    private var filteredDataList: List<DataClass_spl> = dataList

    interface OnItemClickListener {
        fun onItemClick(datax: DataClass_spl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder_spl {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spl_recyclerview, parent, false)
        return DataViewHolder_spl(view)
    }

    override fun onBindViewHolder(
        holder: DataViewHolder_spl,
        position: Int
    ) {
        val item = filteredDataList[position]
        holder.idl.text = item.idL
        holder.spl.text = item.SPL
        holder.date.text = item.date
        holder.start.text = item.start
        holder.end.text = item.end
        holder.status.text = item.status
        holder.remark.text = item.remark

        holder.bind(item, itemClickListener)
    }

    override fun getItemCount(): Int {
        return filteredDataList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                Log.d("Filter", "Performing filtering with constraint: $constraint")
                val charString = constraint?.toString() ?: ""
                filteredDataList = if (charString.isEmpty()) {
                    dataList
                } else {
                    dataList.filter {
                        it.date.contains(charString, true)
                    }
                }
//                Log.d("Filter", "Filtered data size: ${filteredDataList.size}")
                return FilterResults().apply { values = filteredDataList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                Log.d("Filter", "Publishing results for constraint: $constraint")
                filteredDataList = results?.values as? List<DataClass_spl> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}