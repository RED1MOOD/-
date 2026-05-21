package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "recurring_transactions")
data class RecurringTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: String, // "INCOME" or "EXPENSE"
    val category: String,
    val dayOfMonth: Int,
    val lastProcessedTimestamp: Long = 0L
)

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isIncome: Boolean,
    val iconName: String,
    val colorHex: String,
    val isDefault: Boolean = false
)

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timeString: String, // e.g. "09:00"
    val type: String, // "DAILY_EXPENSE" or "BILL_PAYMENT"
    val dateDay: Int = 1,
    val isEnabled: Boolean = true
)

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun clearAll()
}

@Dao
interface RecurringTransactionDao {
    @Query("SELECT * FROM recurring_transactions")
    fun getAllRecurring(): Flow<List<RecurringTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecurring(recurring: RecurringTransaction)

    @Update
    suspend fun updateRecurring(recurring: RecurringTransaction)

    @Delete
    suspend fun deleteRecurring(recurring: RecurringTransaction)
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders")
    fun getAllReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}

@Database(
    entities = [Transaction::class, RecurringTransaction::class, CategoryEntity::class, Reminder::class],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class FinanceRepository(private val db: FinanceDatabase) {
    val allTransactions: Flow<List<Transaction>> = db.transactionDao().getAllTransactions()
    val allRecurring: Flow<List<RecurringTransaction>> = db.recurringTransactionDao().getAllRecurring()
    val allCategories: Flow<List<CategoryEntity>> = db.categoryDao().getAllCategories()
    val allReminders: Flow<List<Reminder>> = db.reminderDao().getAllReminders()

    suspend fun insertTransaction(transaction: Transaction) {
        db.transactionDao().insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        db.transactionDao().deleteTransaction(transaction)
    }

    suspend fun clearTransactions() {
        db.transactionDao().clearAll()
    }

    suspend fun insertRecurring(recurring: RecurringTransaction) {
        db.recurringTransactionDao().insertRecurring(recurring)
    }

    suspend fun updateRecurring(recurring: RecurringTransaction) {
        db.recurringTransactionDao().updateRecurring(recurring)
    }

    suspend fun deleteRecurring(recurring: RecurringTransaction) {
        db.recurringTransactionDao().deleteRecurring(recurring)
    }

    suspend fun insertCategory(category: CategoryEntity) {
        db.categoryDao().insertCategory(category)
    }

    suspend fun updateCategory(category: CategoryEntity) {
        db.categoryDao().updateCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) {
        db.categoryDao().deleteCategory(category)
    }

    suspend fun insertReminder(reminder: Reminder) {
        db.reminderDao().insertReminder(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) {
        db.reminderDao().updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: Reminder) {
        db.reminderDao().deleteReminder(reminder)
    }

    suspend fun checkAndPopulateDefaultCategories() {
        val currentCats = allCategories.first()
        if (currentCats.isEmpty()) {
            val defaults = listOf(
                CategoryEntity(name = "طعام", isIncome = false, iconName = "Restaurant", colorHex = "#F59E0B", isDefault = true),
                CategoryEntity(name = "مواصلات", isIncome = false, iconName = "DirectionsCar", colorHex = "#14B8A6", isDefault = true),
                CategoryEntity(name = "فواتير", isIncome = false, iconName = "Receipt", colorHex = "#3B82F6", isDefault = true),
                CategoryEntity(name = "ترفيه", isIncome = false, iconName = "SportsEsports", colorHex = "#EC4899", isDefault = true),
                CategoryEntity(name = "ملابس", isIncome = false, iconName = "ShoppingBag", colorHex = "#8B5CF6", isDefault = true),
                CategoryEntity(name = "أدوية", isIncome = false, iconName = "MedicalServices", colorHex = "#EF4444", isDefault = true),
                CategoryEntity(name = "راتب", isIncome = true, iconName = "Work", colorHex = "#10B981", isDefault = true),
                CategoryEntity(name = "إستثمار", isIncome = true, iconName = "TrendingUp", colorHex = "#059669", isDefault = true),
                CategoryEntity(name = "قط/منحة", isIncome = true, iconName = "CardGiftcard", colorHex = "#EAB308", isDefault = true),
                CategoryEntity(name = "أخرى", isIncome = false, iconName = "Category", colorHex = "#6B7280", isDefault = true)
            )
            for (def in defaults) {
                db.categoryDao().insertCategory(def)
            }
        }
    }
}
