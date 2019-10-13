package com.example.expensetracker20

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_new_transaction.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<TextView>(R.id.start_date_etxt).text = budget?.startDate
        findViewById<TextView>(R.id.weekly_goal_settings_etxt).text = budget?.weeklyGoal.toString()
        findViewById<TextView>(R.id.currency_etxt).text = budget?.currency

    }

    fun onClickUpdate(view: View){
        when(view){
            update_btn -> {
                if(checkValid()){
                    budget?.startDate = start_date_etxt.text.toString()
                    budget?.weeklyGoal = weekly_goal_settings_etxt.text.toString().toDouble()
                    budget?.currency = currency_etxt.text.toString()
                    saveBudget()
                }
            }
        }
    }

    private fun checkValid():Boolean{
        val date = start_date_etxt.text.toString()
        val value = weekly_goal_settings_etxt.text.toString()

        var decimalPoint = false
        var valErr = ""
        var dateErr = ""
        var err = "Updating the settings"


        //does it work if the last value is a decimal point i.e. 20.
        //Check for error in the value

        for (i in value.indices) {
            if (((value[i].toByte() < 48 || value[i].toByte() > 57) && value[i].toByte() != '.'.toByte()) || (value[i].toByte() == '.'.toByte() && decimalPoint)) {
                valErr = "Goal must be a decimal number"
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
