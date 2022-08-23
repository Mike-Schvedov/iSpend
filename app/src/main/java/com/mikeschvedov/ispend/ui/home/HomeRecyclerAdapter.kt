package com.mikeschvedov.ispend.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.ispend.R
import com.mikeschvedov.ispend.data.database.entities.Expense
import javax.inject.Inject

class HomeRecyclerAdapter @Inject constructor(private val listener: OnItemClickListener) :
        RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

        var list: MutableList<Expense> = mutableListOf()

        fun interface OnItemClickListener{
            fun onItemClicked(
                item: Expense
            )
        }

        // add new data
        fun setNewData(newData: List<Expense>) {
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

        class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var description: TextView =
                itemView.findViewById(R.id.description_txt)
            var amount: TextView =
                itemView.findViewById(R.id.amount_txt)
            var date: TextView =
                itemView.findViewById(R.id.date_txt)
            var category: TextView =
                itemView.findViewById(R.id.category_txt)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
            return HomeViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.single_view_holder,
                    parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

            val item = list[position]

            // --- Setting the description --- //
            holder.description.text = item.description

            // --- Setting the amount --- //
            holder.amount.text = "₪ ${item.amountSpent.toString()}"

            // --- Setting the date --- //
            holder.date.text = "${item.day}/${item.month}/${item.year}"

            // --- Setting the category --- //
            holder.category.text = item.category.hebrew


            //TODO add option to delete expense if clicked on, with alert of course
            // -- Sending the clicked item as callback -- //
//            holder.itemLayout.setOnClickListener {
//                listener.onItemClicked(item)

//            }

        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    class DiffUtilCallbackShips(
        private val oldList: List<Expense>,
        private val newList: List<Expense>
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
            return oldItem.description == newItem.description
        }

        // check if contents are same
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }
    }