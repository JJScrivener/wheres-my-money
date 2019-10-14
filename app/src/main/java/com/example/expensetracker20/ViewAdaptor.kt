package com.example.expensetracker20

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_transaction_item.view.*

class ViewAdaptor : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recItem = layoutInflater.inflate(R.layout.layout_transaction_item, parent, false)
        return CustomViewHolder(recItem)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.transaction_ID_rec?.text = position.plus(1).toString()
        holder.view.transaction_date_rec?.text = budget.getTransactionDate(position).take(5)
        holder.view.transaction_item_rec?.text = budget.getTransactionDescription(position)

        val value = if(budget.getTransactionValue(position).rem(1) == 0.0){
            "${budget.getCurrency()}${budget.getTransactionValue(position).toInt().toString()}"
        }else{
            "${budget.getCurrency()}${budget.getTransactionValue(position).toString()}"
        }
        holder.view.transaction_value_rec?.text = value
    }

    override fun getItemCount(): Int {
        return budget.getSize()
    }


}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
