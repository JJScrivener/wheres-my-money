package com.example.expensetracker20

import android.util.Log
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/*
    Author: Jen
    Date: August 2019

    Description: The com.example.expensetracker20.Budget class manages a list of Transactions which is sorted by date. The list describes all the
        income and expenses for the budget.
 */

class Budget(var startDate: String) : Serializable {
    val transactions: MutableList<Transaction> = mutableListOf()
    var weeklyGoal = 100.0
    var currency = ""


    /*
        Parameters: -
        Returns: The current balance of the transaction expenses and incomes.
        Description: Cycles through the list of transactions and adds all the values together.
     */
    fun getBalance(): Double {
        var balance = 0.0
        for (i in transactions.indices) {
            balance += transactions[i].value
        }
        return balance
    }

    /*
        Parameters: transaction - The new com.example.expensetracker20.Transaction to be added to the budget.
        Returns: -
        Description: Adds the new transaction in the correct chronological order.
    */
    fun addTransaction(transaction: Transaction) {
        if (transactions.size == 0) {
            transactions.add(transaction)
        } else {
            var index = 0
            var indexDate =
                LocalDate.parse(transactions[index].date, DateTimeFormatter.ofPattern(dateFormat))
            val transDate =
                LocalDate.parse(transaction.date, DateTimeFormatter.ofPattern(dateFormat))
            while (true) {
                if (indexDate.isBefore(transDate.plusDays(1))) {
                    index++
                } else {
                    transactions.add(index, transaction)
                    break
                }
                if (index == transactions.size) {
                    transactions.add(transaction)
                    break
                }

                indexDate =
                    LocalDate.parse(
                        transactions[index].date, DateTimeFormatter.ofPattern(dateFormat)
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
        for (i in transactions.indices) {
            println("date: ${transactions[i].date.toString()}, value: ${transactions[i].value}, description: ${transactions[i].description}")
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
        var periodDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(dateFormat))

        pBal.add(0.0)
        var index = 0
        while (index < transactions.size) {

            val indexDate =
                LocalDate.parse(transactions[index].date, DateTimeFormatter.ofPattern(dateFormat))

            // Checks if the transaction in is the current period or not. This assumes that transactions[] is ordered by date.
            if ((indexDate.dayOfMonth == periodDate.dayOfMonth || period != "daily") &&
                (indexDate.isBefore(periodDate.plusDays(7)) || period != "weekly") &&
                (indexDate.month == periodDate.month || period == "weekly") &&
                (indexDate.year == periodDate.year || period == "weekly")
            ) {
                pBal[pBalIndex] += transactions[index].value
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
        var date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(dateFormat))
        val today = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern(dateFormat))
        var thisWeekBalance = 0.0

        while (date.plusWeeks(1).isBefore(today)) {
            date = date.plusWeeks(1)
        }


        for (index in transactions.indices) {

            val indexDate =
                LocalDate.parse(transactions[index].date, DateTimeFormatter.ofPattern(dateFormat))
            if (indexDate.isBefore(date.plusWeeks(1)) && indexDate.isAfter(date)) {
                thisWeekBalance += transactions[index].value
            }
        }
        return thisWeekBalance
    }

    fun deleteTransactions() {
        for (i in transactions.indices) {
            transactions.removeAt(0)
        }
    }

    fun getSize(): Int {
        return transactions.size
    }
}