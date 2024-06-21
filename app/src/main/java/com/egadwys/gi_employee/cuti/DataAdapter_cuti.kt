package com.egadwys.gi_employee.cuti

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataAdapter_cuti(
    private var dataList: List<DataClass_cuti>,
    private val itemClickListener: Cuti
) : RecyclerView.Adapter<DataViewHolder_cuti>(), Filterable {

    private var filteredDataList: List<DataClass_cuti> = dataList

    interface OnItemClickListener {
        fun onItemClick(datax: DataClass_cuti)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder_cuti {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cuti_recyclerview, parent, false)
        return DataViewHolder_cuti(view)
    }

    override fun onBindViewHolder(
        holder: DataViewHolder_cuti,
        position: Int
    ) {
        val item = filteredDataList[position]
        holder.idl.text = item.id
        holder.start.text = item.dateStart
        holder.end.text = item.dateEnd
        holder.remark.text = item.paidLeave

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
                        it.dateStart.contains(charString, true)
                    }
                }
//                Log.d("Filter", "Filtered data size: ${filteredDataList.size}")
                return FilterResults().apply { values = filteredDataList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                Log.d("Filter", "Publishing results for constraint: $constraint")
                filteredDataList = results?.values as? List<DataClass_cuti> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}