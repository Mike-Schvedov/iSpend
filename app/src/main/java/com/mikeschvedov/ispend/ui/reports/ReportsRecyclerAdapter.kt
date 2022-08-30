package com.mikeschvedov.ispend.ui.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.data.database.entities.Expense
import com.mikeschvedov.ispend.models.Report
import javax.inject.Inject

class ReportsRecyclerAdapter @Inject constructor() :
    RecyclerView.Adapter<ReportsRecyclerAdapter.ReportViewHolder>() {

    var list: MutableList<Report> = mutableListOf()

    // add new data
    fun setNewData(newData: List<Report>) {
        // passing the new and old list into the callback
        val diffCallback = DiffUtilCallbackShips(list, newData)
        // we get the result
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        // we clear the old list
        list.clear()
        // and replace it with the new list
        list.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryName: TextView =
            itemView.findViewById(R.id.category_name_tv)
        var amount: TextView =
            itemView.findViewById(R.id.total_amount_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_view_holder_reports,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {

        val item = list[position]

        // --- Setting the category name --- //
        holder.categoryName.text = item.category

        // --- Setting the amount for entire category --- //
        holder.amount.text = "₪ ${item.totalSpent.toString()}"

        if (item.category.equals("סה״כ")){
            holder.categoryName.textSize = 22f
            holder.amount.textSize = 22f
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class DiffUtilCallbackShips(
    private val oldList: List<Report>,
    private val newList: List<Report>
) :
    DiffUtil.Callback() {

    // old size
    override fun getOldListSize(): Int = oldList.size

    // new list size
    override fun getNewListSize(): Int = newList.size

    // if items are same
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.category == newItem.category
    }

    // check if contents are same
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}