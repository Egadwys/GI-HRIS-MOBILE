package com.egadwys.gi_employee

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView

class YourDataAdapter(
    private var dataList: List<YourDataClass>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<YourDataViewHolder>(), Filterable {

    private var filteredDataList: List<YourDataClass> = dataList

    interface OnItemClickListener {
        fun onItemClick(datax: YourDataClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourDataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_your_data, parent, false)
        return YourDataViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: YourDataViewHolder,
        position: Int
    ) {
        val item = filteredDataList[position]
        holder.name.text = item.name
        holder.NIK.text = item.NIK.toString()
        holder.position.text = item.position
        holder.dept.text = item.dept
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
                        it.name.contains(charString, true)
                    }
                }
//                Log.d("Filter", "Filtered data size: ${filteredDataList.size}")
                return FilterResults().apply { values = filteredDataList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                Log.d("Filter", "Publishing results for constraint: $constraint")
                filteredDataList = results?.values as? List<YourDataClass> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}