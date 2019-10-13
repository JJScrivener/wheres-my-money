package com.example.expensetracker20

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_transactions_view.*

class TransactionsViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions_view)

        transactions_rec.layoutManager = LinearLayoutManager(this)
        transactions_rec.adapter = ViewAdaptor()
    }

    fun onClickRemove(view: View) {
        when (view) {
            remove_transaction_btn -> {
                val index = transaction_ID_etxt.text.toString().toInt().minus(1)
                val itemCount = budget?.transactions?.size?.minus(index + 1)

                budget?.transactions?.removeAt(index)
                transactions_rec.adapter?.notifyItemRemoved(index)
                transactions_rec.adapter?.notifyItemRangeChanged(index, itemCount!!)
                saveBudget()
                transaction_ID_etxt.text.clear()
            }
        }
    }

    private fun saveBudget() {
        val sharedPref = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(budget)
        editor.putString("task list", json)
        editor.apply()
    }

}

