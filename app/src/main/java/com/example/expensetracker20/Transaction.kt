package com.example.expensetracker20/*
    Author: Jen
    Date: August 2019

    Description: The com.example.expensetracker20.Transaction class is a container for the different elements of a transaction.
        A transaction consists of a Date, a Value and a Description. A typical transaction might
        look like this:
                    (19/12/2019, -2.30, "1 litre of milk")
                    (19/12/2019, 845.68, "Salary for the week from 11/12-18/12")

    Note1: Expenses have a negative value while incomes have a positive value.
    Note2: com.example.expensetracker20.Transaction values are unit-less, they could be AUD or JPY or Monopoly money
 */

class Transaction(val date: String, val value: Double, val description: String)
