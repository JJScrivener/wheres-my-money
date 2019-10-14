package com.example.expensetracker20

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_new_transaction.*


class NewTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        findViewById<TextView>(R.id.transaction_date_etxt).text = currentDate
    }

    fun onClickAdd(view: View) {
        val date = transaction_date_etxt.text.toString()
        val valueString = transaction_value_etxt.text.toString()
        if (budget.checkValid(
                date,
                valueString,
                "Adding transaction to the budget",
                applicationContext
            )
        ) {

            val value = if (expense_swt.isChecked) {
                transaction_value_etxt.text.toString().toDouble().times(-1)
            } else {
                transaction_value_etxt.text.toString().toDouble()
            }

            val description = transaction_description_etxt.text.toString()
            budget.addTransaction(date, value, description)

            transaction_description_etxt.text.clear()
            transaction_value_etxt.text.clear()
        }
    }


    override fun onPause() {
        saveBudget(applicationContext)
        super.onPause()
    }
}
