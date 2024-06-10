package com.egadwys.gi_employee.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.egadwys.gi_employee.R

class DataAdapter_attendance(
    private var dataList: List<DataClass_attendance>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DataViewHolder_attendance>(), Filterable {

    private var filteredDataList: List<DataClass_attendance> = dataList

    interface OnItemClickListener {
        fun onItemClick(datax: DataClass_attendance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder_attendance {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendence_recyclerview, parent, false)
        return DataViewHolder_attendance(view)
    }

    override fun onBindViewHolder(
        holder: DataViewHolder_attendance,
        position: Int
    ) {
        val item = filteredDataList[position]
        holder.hari.text = item.hari
        holder.tanggal.text = item.tanggal
        holder.tin.text = item.masuk
        holder.tout.text = item.pulang

        holder.bind(item, itemClickListener)
    }

//    @SuppressLint("SetTextI18n")
//    override fun onBindViewHolder(holder: YourDataViewHolder, position: Int) {
//        val item = filteredDataList[position]
//        holder.name.text = item.name
//        holder.NIK.text = item.NIK.toString()
//        holder.position.text = item.position
//        holder.dept.text = item.dept
//        holder.tin.text = item.masuk
//        holder.tout.text = item.pulang
//
//        holder.bind(item, itemClickListener)
//    }

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
                        it.tanggal.contains(charString, true)
                    }
                }
//                Log.d("Filter", "Filtered data size: ${filteredDataList.size}")
                return FilterResults().apply { values = filteredDataList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                Log.d("Filter", "Publishing results for constraint: $constraint")
                filteredDataList = results?.values as? List<DataClass_attendance> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}