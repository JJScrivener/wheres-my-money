package com.example.expensetracker20

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val dateFormat = "dd/MM/yyyy"
const val SHARED_PREFS = "shared preferences"
const val BUDGET = "budget"

var budget = Budget("01/01/2019")
val calendar: Calendar = Calendar.getInstance()
val format = SimpleDateFormat(dateFormat)
val currentDate: String = format.format(calendar.time)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBudget(applicationContext)
        updateBudgetViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings_menu_btn -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.view_transactions_menu_btn -> {
            val intent = Intent(this, TransactionsViewActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.add_transaction_menu_btn -> {
            val intent = Intent(this, NewTransactionActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        saveBudget(applicationContext)
        super.onPause()
    }

    override fun onResume() {
        updateBudgetViews()
        super.onResume()
    }

    fun onClick(view: View) {
        when (view) {
            new_transaction_btn -> {
                val intent = Intent(this, NewTransactionActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateBudgetViews() {

        val balance = "${budget.getCurrency()} ${budget.getBalance().toString()}"
        val thisWeek = budget.getThisWeekBalance().times(-1)
        val thisWeekRemaining = budget.getWeeklyGoal().minus(thisWeek)

        balance_dtxt.text = balance

        weekly_goal_dtxt.text = budget.getWeeklyGoal().toString()

        weekly_expenditure_dtxt.text = thisWeek.toString()

        weekly_remaining_dtxt.text = thisWeekRemaining.toString()

    }

}

fun saveBudget(context: Context) {
    val sharedPref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    val gson = Gson()
    val json = gson.toJson(budget)
    editor.putString(BUDGET, json)
    editor.apply()
}

fun loadBudget(context: Context) {
    val sharedPref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    val gson = Gson()
    val json = sharedPref.getString(BUDGET, null)
    val type = object : TypeToken<Budget>() {}.type

    val temp: Budget? = gson.fromJson(json, type)

    if (temp != null) {
        budget = temp
    }
}