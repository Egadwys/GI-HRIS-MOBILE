package com.egadwys.gi_employee.payroll

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataAdapter_payroll(
    private var dataList: List<DataClass_payroll>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DataViewHolder_payroll>(), Filterable {

    private var filteredDataList: List<DataClass_payroll> = dataList

    interface OnItemClickListener {
        fun onItemClick(datax: DataClass_payroll)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder_payroll {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.payroll_recyclerview, parent, false)
        return DataViewHolder_payroll(view)
    }

    override fun onBindViewHolder(
        holder: DataViewHolder_payroll,
        position: Int
    ) {
        val item = filteredDataList[position]
        holder.periode.text = item.periode

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
                        it.periode.contains(charString, true)
                    }
                }
//                Log.d("Filter", "Filtered data size: ${filteredDataList.size}")
                return FilterResults().apply { values = filteredDataList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                Log.d("Filter", "Publishing results for constraint: $constraint")
                filteredDataList = results?.values as? List<DataClass_payroll> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}