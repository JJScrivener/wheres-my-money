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
        holder.view.transaction_date_rec?.text = budget?.transactions?.get(position)?.date?.take(5)
        holder.view.transaction_item_rec?.text = budget?.transactions?.get(position)?.description

        val value = if(budget?.transactions?.get(position)?.value?.rem(1) == 0.0){
            "${budget?.currency}${budget?.transactions?.get(position)?.value?.toInt().toString()}"
        }else{
            "${budget?.currency}${budget?.transactions?.get(position)?.value.toString()}"
        }
        holder.view.transaction_value_rec?.text = value
    }

    override fun getItemCount(): Int {
        return budget!!.transactions.size
    }


}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view)
