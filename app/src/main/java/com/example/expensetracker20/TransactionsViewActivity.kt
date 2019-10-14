package com.example.expensetracker20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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
                if (validTransactionID(transaction_ID_etxt.text.toString(), budget.getSize())) {
                    val index = transaction_ID_etxt.text.toString().toInt().minus(1)
                    val itemCount = budget.getSize().minus(index + 1)

                    budget.removeTransaction(index)
                    transactions_rec.adapter?.notifyItemRemoved(index)
                    transactions_rec.adapter?.notifyItemRangeChanged(index, itemCount)
                    transaction_ID_etxt.text.clear()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Not a valid transaction ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun validTransactionID(ID: String, size: Int): Boolean {
        val valid: Boolean

        for (i in ID.indices) {
            if (ID[i] !in '0'..'9') {
                valid = false
                return valid
            }
        }

        valid = ID.toInt() in 1..size

        return valid
    }

    override fun onPause() {
        saveBudget(applicationContext)
        super.onPause()
    }
}