package com.example.expensetracker20

import android.content.Context
import android.widget.Toast
import java.io.Serializable
import java.lang.NumberFormatException
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/*
    Author: Jen
    Date: August 2019

    Description: The com.example.expensetracker20.Budget class manages a list of Transactions which is sorted by date. The list describes all the
        income and expenses for the budget.
 */

class Budget(newStartDate: String) : Serializable {

    private class Transaction(val date: String, val value: Double, val description: String)

    private val transactions: MutableList<Transaction> = mutableListOf()
    private var weeklyGoal = 100.0
    private var currency = ""
    private var startDate = newStartDate


    /*
        Parameters: -
        Returns: The current balance of the transaction expenses and incomes.
        Description: Cycles through the list of transactions and adds all the values together.
     */
    fun getBalance(): Double {
        var balance = 0.0
        for (i in this.transactions.indices) {
            balance += this.transactions[i].value
        }
        return balance
    }

    fun addTransaction(date: String, value: Double, description: String) {
        val transaction = Transaction(date, value, description)
        if (this.transactions.size == 0) {
            this.transactions.add(transaction)
        } else {
            var index = 0
            var indexDate =
                LocalDate.parse(
                    this.transactions[index].date,
                    DateTimeFormatter.ofPattern(dateFormat)
                )
            val transDate =
                LocalDate.parse(transaction.date, DateTimeFormatter.ofPattern(dateFormat))
            while (true) {
                if (indexDate.isBefore(transDate.plusDays(1))) {
                    index++
                } else {
                    this.transactions.add(index, transaction)
                    break
                }
                if (index == transactions.size) {
                    this.transactions.add(transaction)
                    break
                }

                indexDate =
                    LocalDate.parse(
                        this.transactions[index].date, DateTimeFormatter.ofPattern(dateFormat)
                    )
            }

        }
    }

    /*
        Parameters: -
        Returns: -
        Description: Prints the budget transactions. Used for debugging.
    */
    fun printBudget() {
        for (i in this.transactions.indices) {
            println("date: ${this.transactions[i].date}, value: ${this.transactions[i].value}, description: ${this.transactions[i].description}")
        }
    }

    /*
        Parameters: period - The period by which to group the balance.
            -daily: The balance for each day
            -weekly: The balance for the week starting from the budget start date's day of the week
            -monthly: The balance for the calendar month
        Returns: A list of doubles with the balance for each period.
        Description: Checks the desired period before calculating and returning the balance for each period in a list.
    */
    fun getPeriodicBalance(period: String): MutableList<Double> {

        val pBal: MutableList<Double> = mutableListOf()
        var pBalIndex = 0
        var periodDate = LocalDate.parse(this.startDate, DateTimeFormatter.ofPattern(dateFormat))

        pBal.add(0.0)
        var index = 0
        while (index < this.transactions.size) {

            val indexDate =
                LocalDate.parse(
                    this.transactions[index].date,
                    DateTimeFormatter.ofPattern(dateFormat)
                )

            // Checks if the transaction in is the current period or not. This assumes that transactions[] is ordered by date.
            if ((indexDate.dayOfMonth == periodDate.dayOfMonth || period != "daily") &&
                (indexDate.isBefore(periodDate.plusDays(7)) || period != "weekly") &&
                (indexDate.month == periodDate.month || period == "weekly") &&
                (indexDate.year == periodDate.year || period == "weekly")
            ) {
                pBal[pBalIndex] += this.transactions[index].value
                index++
            } else {
                pBal.add(0.0)
                pBalIndex++
                when (period) {
                    "daily" -> periodDate = periodDate.plusDays(1)
                    "weekly" -> periodDate = periodDate.plusDays(7)
                    "monthly" -> periodDate = periodDate.plusMonths(1)
                }

            }
        }


        return pBal
    }

    fun getThisWeekBalance(): Double {
        var date = LocalDate.parse(this.startDate, DateTimeFormatter.ofPattern(dateFormat))
        val today = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern(dateFormat))
        var thisWeekBalance = 0.0

        while (date.plusWeeks(1).isBefore(today)) {
            date = date.plusWeeks(1)
        }


        for (index in transactions.indices) {

            val indexDate =
                LocalDate.parse(
                    this.transactions[index].date,
                    DateTimeFormatter.ofPattern(dateFormat)
                )
            if (indexDate.isBefore(date.plusWeeks(1)) && indexDate.isAfter(date)) {
                thisWeekBalance += this.transactions[index].value
            }
        }
        return thisWeekBalance
    }

    fun deleteTransactions() {
        for (i in this.transactions.indices) {
            this.transactions.removeAt(0)
        }
    }

    fun getSize(): Int {
        return this.transactions.size
    }

    fun getWeeklyGoal(): Double {
        return this.weeklyGoal
    }

    fun setWeeklyGoal(goal: Double) {
        this.weeklyGoal = goal
    }

    fun getCurrency(): String {
        return this.currency
    }

    fun setCurrency(currency: String) {
        this.currency = currency
    }

    fun getStartDate(): String{
        return this.startDate
    }

    fun setStartDate(startDate: String) {
        this.startDate = startDate
    }

    fun getTransactionDate(position: Int): String {
        return this.transactions[position].date
    }

    fun getTransactionValue(position: Int): Double {
        return this.transactions[position].value
    }

    fun getTransactionDescription(position: Int): String {
        return this.transactions[position].description
    }

    fun removeTransaction(position: Int){
        transactions.removeAt(position)
    }

    fun checkValid(date: String, value: String, allGood: String, context: Context): Boolean {
        var valErr = ""
        var dateErr = ""
        var err = allGood

        //Check for error in the value
        try {
            value.toDouble()
        } catch (e: NumberFormatException) {
            valErr = "Value must be a decimal number"
        }

        //Check for error in the date
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat))
        } catch (e: DateTimeParseException) {
            dateErr = "Not a valid date, please use format dd/mm/yyyy"
        }

        return if (dateErr == valErr) {
            // no errors
            Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
            true
        } else {
            err = when {
                dateErr == "" -> valErr
                valErr == "" -> dateErr
                else -> "$dateErr also $valErr"
            }
            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
            false
        }
    }
}