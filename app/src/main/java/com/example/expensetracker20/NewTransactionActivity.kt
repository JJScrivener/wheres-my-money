package com.example.expensetracker20

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_transaction.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class NewTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        findViewById<TextView>(R.id.transaction_date_etxt).text = currentDate
    }

    fun onClickAdd(view: View) {
        if (checkValidTransaction()) {
            val date = transaction_date_etxt.text.toString()

            val value = if (expense_swt.isChecked) {
                transaction_value_etxt.text.toString().toDouble().times(-1)
            } else {
                transaction_value_etxt.text.toString().toDouble()
            }

            val description = transaction_description_etxt.text.toString()
            val newTransaction = Transaction(date, value, description)
            budget?.addTransaction(newTransaction)
            saveBudget()


            transaction_description_etxt.text.clear()
            transaction_value_etxt.text.clear()
        }
    }

    private fun checkValidTransaction(): Boolean {
        val date = transaction_date_etxt.text.toString()
        val value = transaction_value_etxt.text.toString()

        var decimalPoint = false
        var valErr = ""
        var dateErr = ""
        var err = "Adding new transaction to the budget"


        //does it work if the last value is a decimal point i.e. 20.
        //Check for error in the value

        for (i in value.indices) {
            if (((value[i].toByte() < 48 || value[i].toByte() > 57) && value[i].toByte() != '.'.toByte()) || (value[i].toByte() == '.'.toByte() && decimalPoint)) {
                valErr = "Value must be a decimal number"
                break
            }
            if (value[i].toByte() == '.'.toByte()) {
                decimalPoint = true
            }
        }

        //Check for error in the date
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))
        } catch (e: DateTimeParseException) {
            dateErr = "Not a valid date, please use format dd/mm/yyyy"
        }

        return if (dateErr == valErr) {
            // no errors
            Toast.makeText(applicationContext, err, Toast.LENGTH_SHORT).show()
            true
        } else {
            err = when {
                dateErr == "" -> valErr
                valErr == "" -> dateErr
                else -> "$dateErr also $valErr"
            }
            Toast.makeText(applicationContext, err, Toast.LENGTH_LONG).show()
            false

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
