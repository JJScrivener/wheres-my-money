package com.example.expensetracker20

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<TextView>(R.id.start_date_etxt).text = budget.getStartDate()
        findViewById<TextView>(R.id.weekly_goal_settings_etxt).text = budget.getWeeklyGoal().toString()
        findViewById<TextView>(R.id.currency_etxt).text = budget.getCurrency()

    }

    fun onClickUpdate(view: View){
        when(view){
            update_btn -> {
                val date = start_date_etxt.text.toString()
                val value = weekly_goal_settings_etxt.text.toString()

                if((budget.checkValid(date,value,"Updating settings",applicationContext)) ){
                    budget.setStartDate(start_date_etxt.text.toString())
                    budget.setWeeklyGoal(weekly_goal_settings_etxt.text.toString().toDouble())
                    budget.setCurrency(currency_etxt.text.toString())
                    saveBudget()
                }
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
