package com.example.expensetracker20

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
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
        if (budget!!.checkValid(date,valueString,"Adding transaction to the budget",applicationContext)) {

            val value = if (expense_swt.isChecked) {
                transaction_value_etxt.text.toString().toDouble().times(-1)
            } else {
                transaction_value_etxt.text.toString().toDouble()
            }

            val description = transaction_description_etxt.text.toString()
            budget?.addTransaction(date, value, description)
            saveBudget()

            transaction_description_etxt.text.clear()
            transaction_value_etxt.text.clear()
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
