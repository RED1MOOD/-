package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CategoryEntity
import com.example.data.FinanceDatabase
import com.example.data.FinanceRepository
import com.example.data.RecurringTransaction
import com.example.data.Reminder
import com.example.data.Transaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FinanceRepository

    val transactions: StateFlow<List<Transaction>>
    val recurring: StateFlow<List<RecurringTransaction>>
    val categories: StateFlow<List<CategoryEntity>>
    val reminders: StateFlow<List<Reminder>>

    init {
        val database = FinanceDatabase.getDatabase(application)
        repository = FinanceRepository(database)

        transactions = repository.allTransactions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        recurring = repository.allRecurring.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        categories = repository.allCategories.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        reminders = repository.allReminders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.checkAndPopulateDefaultCategories()
            processAutomaticRecurringPayments()
        }
    }

    fun addTransaction(title: String, amount: Double, category: String, notes: String) {
        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(
                    title = title,
                    amount = amount,
                    category = category,
                    notes = notes
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    fun clearAllTransactions() {
        viewModelScope.launch {
            repository.clearTransactions()
        }
    }

    fun addRecurringTransaction(title: String, amount: Double, type: String, category: String, dayOfMonth: Int) {
        viewModelScope.launch {
            repository.insertRecurring(
                RecurringTransaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    dayOfMonth = dayOfMonth
                )
            )
            processAutomaticRecurringPayments()
        }
    }

    fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch {
            repository.deleteRecurring(recurringTransaction)
        }
    }

    fun addCategory(name: String, isIncome: Boolean, iconName: String, colorHex: String) {
        viewModelScope.launch {
            repository.insertCategory(
                CategoryEntity(
                    name = name,
                    isIncome = isIncome,
                    iconName = iconName,
                    colorHex = colorHex
                )
            )
        }
    }

    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun addReminder(title: String, timeString: String, type: String, dateDay: Int) {
        viewModelScope.launch {
            repository.insertReminder(
                Reminder(
                    title = title,
                    timeString = timeString,
                    type = type,
                    dateDay = dateDay
                )
            )
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
        }
    }

    fun toggleReminderEnabled(reminder: Reminder) {
        viewModelScope.launch {
            repository.updateReminder(reminder.copy(isEnabled = !reminder.isEnabled))
        }
    }

    /**
     * Scan and auto-process recurring transactions if they are due for the current month.
     */
    fun processAutomaticRecurringPayments() {
        viewModelScope.launch {
            val recList = repository.allRecurring.stateIn(viewModelScope).value
            if (recList.isEmpty()) return@launch

            val now = Calendar.getInstance()
            val currentYear = now.get(Calendar.YEAR)
            val currentMonth = now.get(Calendar.MONTH) // 0 - 11
            val currentDay = now.get(Calendar.DAY_OF_MONTH)

            for (rec in recList) {
                // If the scheduled day of month is reached or passed
                if (currentDay >= rec.dayOfMonth) {
                    val lastProcessed = Calendar.getInstance()
                    if (rec.lastProcessedTimestamp > 0L) {
                        lastProcessed.timeInMillis = rec.lastProcessedTimestamp
                    }

                    val sameMonth = rec.lastProcessedTimestamp > 0L &&
                            lastProcessed.get(Calendar.YEAR) == currentYear &&
                            lastProcessed.get(Calendar.MONTH) == currentMonth

                    if (!sameMonth) {
                        // Mark as processed first to prevent infinite loop or race conditions
                        val updatedRec = rec.copy(lastProcessedTimestamp = now.timeInMillis)
                        repository.updateRecurring(updatedRec)

                        // Insert actual transaction
                        val finalAmount = if (rec.type == "INCOME") rec.amount else -rec.amount
                        repository.insertTransaction(
                            Transaction(
                                title = rec.title + " (دوري تلقائي)",
                                amount = finalAmount,
                                category = rec.category,
                                notes = "عملية مجدولة تم تسجيلها تلقائياً ليوم ${rec.dayOfMonth} في الشهر."
                            )
                        )
                    }
                }
            }
        }
    }
}
