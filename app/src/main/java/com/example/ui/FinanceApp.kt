package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.collectAsState
import com.example.data.CategoryEntity
import com.example.data.RecurringTransaction
import com.example.data.Reminder
import com.example.data.Transaction
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// Obsidian Palette & Material 3 Dark Luxury Aesthetics
val ObsidianBackground = Color(0xFF0A0A0C)
val DeepCardBg = Color(0xFF141417)
val LightCardBg = Color(0xFF1E1E22)
val GoldAccent = Color(0xFFFFD700)
val CashGreen = Color(0xFF10B981)
val DebitRed = Color(0xFFEF4444)
val TextWhite = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF8E8E93)

// Multi-Lingual Translation Helper Object
object Trans {
    fun get(key: String, lang: String): String {
        val en = mapOf(
            "app_title" to "Hisabati Finance",
            "greeting" to "Welcome to Hisabati 👋",
            "subtitle" to "Manage and track your personal wealth with obsidian elegance",
            "balance" to "Current Total Balance",
            "income" to "Income (+)",
            "expense" to "Expenses (-)",
            "chart_title" to "Expense Breakdown by Category",
            "total_expenditure" to "Total Expenditure",
            "new_income" to "New Income",
            "new_expense" to "New Expense",
            "recent_tx" to "Recent Transactions",
            "no_tx" to "No transactions logged yet.",
            "categories" to "Categories",
            "add_category" to "Add Category",
            "category_desc" to "Custom budget and tracking categories",
            "schedule" to "Scheduled",
            "schedule_desc" to "Manage monthly agreements, subscriptions, and wages",
            "add_schedule" to "Schedule Action",
            "no_schedule" to "No recurring payments set up.",
            "reminders" to "Reminders",
            "reminders_desc" to "Be notified of bill dates and daily logs",
            "add_reminder" to "New Reminder",
            "no_reminders" to "No active billing notifications.",
            "settings" to "Settings",
            "settings_desc" to "Tailor Hisabati to your preference",
            "language" to "Application Language",
            "currency" to "Preferred Currency",
            "clear_data" to "Clear All Data",
            "clear_warning" to "Are you sure you want to completely erase your records? This cannot be undone.",
            "confirm" to "Confirm",
            "cancel" to "Cancel",
            "notes" to "Notes",
            "title" to "Title",
            "amount" to "Amount",
            "category" to "Category",
            "save" to "Save",
            "day" to "Day of Month",
            "time" to "Time (HH:mm)",
            "type" to "Type",
            "enabled" to "Enabled",
            "disabled" to "Disabled",
            "Food" to "Food",
            "Transport" to "Transportation",
            "Bills" to "Bills & Utilities",
            "Entertainment" to "Entertainment",
            "Clothing" to "Clothing",
            "Health" to "Health & Meds",
            "Salary" to "Salary / Wage",
            "Investment" to "Investments",
            "Gift" to "Gifts / Awards",
            "Others" to "Others",
            "add_tx" to "Add Transaction",
            "income_term" to "Income",
            "expense_term" to "Expense",
            "daily_exp" to "Daily Expense",
            "bill_pay" to "Bill Payment",
            "due_on" to "Due on day",
            "every_month" to "every month at"
        )
        val ar = mapOf(
            "app_title" to "حساباتي المالية",
            "greeting" to "مرحباً بك في حساباتي 👋",
            "subtitle" to "أدر وتتبع أموالك الخاصة بكل يسر وسهولة واحترافية",
            "balance" to "الرصيد الكلي الحالي",
            "income" to "الواردات (+)",
            "expense" to "المصروفات (-)",
            "chart_title" to "تفصيل بياني للمصروفات حسب الفئة",
            "total_expenditure" to "إجمالي النفقات",
            "new_income" to "وارد جديد",
            "new_expense" to "مصروف جديد",
            "recent_tx" to "آخر المعاملات والعمليات المسجلة",
            "no_tx" to "لا توجد عمليات مسجلة حالياً.",
            "categories" to "التصنيفات",
            "add_category" to "إضافة فئة",
            "category_desc" to "تصنيفات الدخل والمصاريف المخصصة لتنظيم عملياتك",
            "schedule" to "المجدولة",
            "schedule_desc" to "الأقساط الشهرية، الرواتب والاشتراكات المقيدة آلياً",
            "add_schedule" to "جدولة معاملة",
            "no_schedule" to "لا يوجد قيود مجدولة شهرياً حالياً.",
            "reminders" to "التنبيهات",
            "reminders_desc" to "تذكير بخصوص تعقب وتسجيل المصروفات اليومية والفواتير",
            "add_reminder" to "تذكير جديد",
            "no_reminders" to "لا توجد تذكيرات مميزة حالياً.",
            "settings" to "الإعدادات",
            "settings_desc" to "اضبط خيارات وتفضيلات التطبيق الأساسية",
            "language" to "لغة التطبيق",
            "currency" to "العملة الرئيسية",
            "clear_data" to "مسح كافة البيانات",
            "clear_warning" to "هل أنت متأكد تمامًا من مسح وحذف كافة العمليات والبيانات التراكمية؟ لا يمكن الاسترجاع لاحقاً.",
            "confirm" to "تأكيد",
            "cancel" to "إلغاء",
            "notes" to "ملاحظات",
            "title" to "العنوان",
            "amount" to "المبلغ",
            "category" to "التصنيف",
            "save" to "حفظ",
            "day" to "يوم الاستحقاق",
            "time" to "الوقت (ساعة:دقيقة)",
            "type" to "النوع",
            "enabled" to "مفعل",
            "disabled" to "معطل",
            "Food" to "طعام",
            "Transport" to "مواصلات",
            "Bills" to "فواتير",
            "Entertainment" to "ترفيه",
            "Clothing" to "ملابس",
            "Health" to "أدوية / صحة",
            "Salary" to "راتب",
            "Investment" to "إستثمار",
            "Gift" to "قط/منحة",
            "Others" to "أخرى",
            "add_tx" to "إضافة عملية جديدة",
            "income_term" to "وارد",
            "expense_term" to "مصروف",
            "daily_exp" to "مصروف يومي",
            "bill_pay" to "دفع فاتورة",
            "due_on" to "تستحق يوم",
            "every_month" to "من كل شهر الساعة"
        )
        return if (lang == "ar") {
            ar[key] ?: en[key] ?: key
        } else {
            en[key] ?: key
        }
    }
}

// Format Amount with Custom Selected Currency Symbol
fun formatCurrencyAmount(amount: Double, currency: String): String {
    val df = DecimalFormat("#,###.##")
    val formatted = df.format(amount)
    return when (currency) {
        "USD" -> "$$formatted"
        "EUR" -> "€$formatted"
        "EGP" -> "$formatted EGP"
        "SAR" -> "$formatted SAR"
        else -> "$formatted $"
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val parsed = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(0xFF000000 or parsed)
        } else {
            Color(parsed)
        }
    } catch (e: Exception) {
        Color.Gray
    }
}

fun mapIconNameToVector(iconName: String): ImageVector {
    return when (iconName) {
        "Restaurant" -> Icons.Default.Restaurant
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "Receipt" -> Icons.Default.Receipt
        "SportsEsports" -> Icons.Default.SportsEsports
        "ShoppingBag" -> Icons.Default.ShoppingBag
        "MedicalServices" -> Icons.Default.MedicalServices
        "School" -> Icons.Default.School
        "FitnessCenter" -> Icons.Default.FitnessCenter
        "Home" -> Icons.Default.Home
        "Build" -> Icons.Default.Build
        "CardGiftcard" -> Icons.Default.CardGiftcard
        "Lightbulb" -> Icons.Default.Lightbulb
        "TrendingUp" -> Icons.Default.TrendingUp
        "Work" -> Icons.Default.Work
        "Domain" -> Icons.Default.Domain
        "Category" -> Icons.Default.Category
        else -> Icons.Default.Category
    }
}

fun getCategoryColorDynamic(categoryName: String, categoriesList: List<CategoryEntity>): Color {
    val cat = categoriesList.find { it.name == categoryName }
    return if (cat != null) parseHexColor(cat.colorHex) else getCategoryColor(categoryName)
}

fun getCategoryIconDynamic(categoryName: String, categoriesList: List<CategoryEntity>): ImageVector {
    val cat = categoriesList.find { it.name == categoryName }
    return if (cat != null) mapIconNameToVector(cat.iconName) else getCategoryIcon(categoryName)
}

fun getCategoryColor(categoryName: String): Color {
    return when (categoryName) {
        "Food" -> Color(0xFFF59E0B)
        "Transport" -> Color(0xFF14B8A6)
        "Bills" -> Color(0xFF3B82F6)
        "Entertainment" -> Color(0xFFEC4899)
        "Clothing" -> Color(0xFF8B5CF6)
        "Health" -> Color(0xFFEF4444)
        "Salary" -> Color(0xFF10B981)
        "Investment" -> Color(0xFF059669)
        "Gift" -> Color(0xFFEAB308)
        else -> Color(0xFF6B7280)
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName) {
        "Food" -> Icons.Default.Restaurant
        "Transport" -> Icons.Default.DirectionsCar
        "Bills" -> Icons.Default.Receipt
        "Entertainment" -> Icons.Default.SportsEsports
        "Clothing" -> Icons.Default.ShoppingBag
        "Health" -> Icons.Default.MedicalServices
        "Salary" -> Icons.Default.Work
        "Investment" -> Icons.Default.TrendingUp
        "Gift" -> Icons.Default.CardGiftcard
        else -> Icons.Default.Category
    }
}

@Composable
fun FinanceApp(viewModel: FinanceViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    
    val transactions by viewModel.transactions.collectAsState(initial = emptyList())
    val recurring by viewModel.recurring.collectAsState(initial = emptyList())
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val reminders by viewModel.reminders.collectAsState(initial = emptyList())

    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    var showAddTxDialog by remember { mutableStateOf(false) }
    var dialogIsIncome by remember { mutableStateOf(false) }
    var showAddRecDialog by remember { mutableStateOf(false) }
    var showAddCatDialog by remember { mutableStateOf(false) }
    var showAddRemDialog by remember { mutableStateOf(false) }

    // Enforce dynamic layout direction based on chosen language (ar = RTL, en = LTR)
    val layoutDir = if (selectedLanguage == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDir) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                background = ObsidianBackground,
                surface = DeepCardBg,
                primary = GoldAccent,
                secondary = CashGreen,
                error = DebitRed
            )
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = DeepCardBg,
                        tonalElevation = 8.dp,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        NavigationBarItem(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = Trans.get("overview", selectedLanguage)) },
                            label = { Text(if (Trans.get("schedule", selectedLanguage) == "Scheduled") "Overview" else "الرئيسية", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBackground,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Category, contentDescription = Trans.get("categories", selectedLanguage)) },
                            label = { Text(Trans.get("categories", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBackground,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Autorenew, contentDescription = Trans.get("schedule", selectedLanguage)) },
                            label = { Text(Trans.get("schedule", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBackground,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            icon = { Icon(Icons.Default.NotificationsActive, contentDescription = Trans.get("reminders", selectedLanguage)) },
                            label = { Text(Trans.get("reminders", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBackground,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                        NavigationBarItem(
                            selected = selectedTab == 4,
                            onClick = { selectedTab = 4 },
                            icon = { Icon(Icons.Default.Settings, contentDescription = Trans.get("settings", selectedLanguage)) },
                            label = { Text(Trans.get("settings", selectedLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ObsidianBackground,
                                selectedTextColor = GoldAccent,
                                indicatorColor = GoldAccent,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ObsidianBackground)
                        .padding(paddingValues)
                ) {
                    when (selectedTab) {
                        0 -> DashboardScreen(
                            transactions = transactions,
                            categoriesList = categories,
                            lang = selectedLanguage,
                            currency = selectedCurrency,
                            onAddTransaction = { isIncome ->
                                dialogIsIncome = isIncome
                                showAddTxDialog = true
                            },
                            onDeleteTransaction = { viewModel.deleteTransaction(it) }
                        )
                        1 -> CategoriesScreen(
                            categories = categories,
                            lang = selectedLanguage,
                            onAddCategoryClick = { showAddCatDialog = true },
                            onDeleteCategory = { viewModel.deleteCategory(it) }
                        )
                        2 -> RecurringScreen(
                            recurring = recurring,
                            categoriesList = categories,
                            lang = selectedLanguage,
                            currency = selectedCurrency,
                            onAddRecurringClick = { showAddRecDialog = true },
                            onDeleteRecurring = { viewModel.deleteRecurringTransaction(it) }
                        )
                        3 -> RemindersScreen(
                            reminders = reminders,
                            lang = selectedLanguage,
                            onAddReminderClick = { showAddRemDialog = true },
                            onToggleReminder = { viewModel.toggleReminderEnabled(it) },
                            onDeleteReminder = { viewModel.deleteReminder(it) }
                        )
                        4 -> SettingsScreen(
                            lang = selectedLanguage,
                            currency = selectedCurrency,
                            onLanguageChange = { viewModel.setLanguage(it) },
                            onCurrencyChange = { viewModel.setCurrency(it) },
                            onClearAll = { viewModel.clearAllTransactions() }
                        )
                    }

                    // Dialog Trigger Logic
                    if (showAddTxDialog) {
                        AddTransactionDialog(
                            isIncome = dialogIsIncome,
                            categoriesList = categories,
                            lang = selectedLanguage,
                            onDismiss = { showAddTxDialog = false },
                            onConfirm = { title, amount, category, notes ->
                                val finalAmount = if (dialogIsIncome) amount else -amount
                                viewModel.addTransaction(title, finalAmount, category, notes)
                                showAddTxDialog = false
                            }
                        )
                    }

                    if (showAddRecDialog) {
                        AddRecurringDialog(
                            categoriesList = categories,
                            lang = selectedLanguage,
                            onDismiss = { showAddRecDialog = false },
                            onConfirm = { title, amount, type, category, dayOfMonth ->
                                viewModel.addRecurringTransaction(title, amount, type, category, dayOfMonth)
                                showAddRecDialog = false
                            }
                        )
                    }

                    if (showAddCatDialog) {
                        AddCategoryDialog(
                            lang = selectedLanguage,
                            onDismiss = { showAddCatDialog = false },
                            onConfirm = { name, isIncome, icon, color ->
                                viewModel.addCategory(name, isIncome, icon, color)
                                showAddCatDialog = false
                            }
                        )
                    }

                    if (showAddRemDialog) {
                        AddReminderDialog(
                            lang = selectedLanguage,
                            onDismiss = { showAddRemDialog = false },
                            onConfirm = { title, time, type, dateDay ->
                                viewModel.addReminder(title, time, type, dateDay)
                                showAddRemDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Screen A: Dashboard & Operations Screen
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun DashboardScreen(
    transactions: List<Transaction>,
    categoriesList: List<CategoryEntity>,
    lang: String,
    currency: String,
    onAddTransaction: (Boolean) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit
) {
    val totalIncome = transactions.filter { it.amount > 0 }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.amount < 0 }.sumOf { -it.amount }
    val balance = totalIncome - totalExpense

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Launcher header banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Trans.get("greeting", lang),
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = Trans.get("subtitle", lang),
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = GoldAccent,
                modifier = Modifier.size(24.dp)
            )
        }

        // Luxury Balance Card with glowing gold border and Brush Gradients
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(LightCardBg, DeepCardBg)
                    )
                )
                .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(Trans.get("balance", lang), color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatCurrencyAmount(balance, currency),
                        color = if (balance >= 0) CashGreen else DebitRed,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(42.dp)
                )
            }
        }

        // Profit / Loss stats breakdown cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Income card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCardBg),
                border = BorderStroke(1.dp, CashGreen.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CashGreen.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = CashGreen, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text(Trans.get("income", lang), color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(formatCurrencyAmount(totalIncome, currency), color = CashGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Expense card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCardBg),
                border = BorderStroke(1.dp, DebitRed.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(DebitRed.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = DebitRed, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text(Trans.get("expense", lang), color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(formatCurrencyAmount(totalExpense, currency), color = DebitRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Native Donut Chart of Categorized Expenses
        if (totalExpense > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DeepCardBg)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = Trans.get("chart_title", lang),
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = if(lang == "ar") TextAlign.Right else TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    val expenseByCategory = transactions
                        .filter { it.amount < 0 }
                        .groupBy { it.category }
                        .mapValues { -it.value.sumOf { tx -> tx.amount } }

                    Box(
                        modifier = Modifier.size(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            var startAngle = -90f
                            expenseByCategory.forEach { (catName, amt) ->
                                val sweep = (amt / totalExpense).toFloat() * 360f
                                drawArc(
                                    color = getCategoryColorDynamic(catName, categoriesList),
                                    startAngle = startAngle,
                                    sweepAngle = sweep,
                                    useCenter = false,
                                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round),
                                    topLeft = Offset(8.dp.toPx(), 8.dp.toPx()),
                                    size = Size(size.width - 16.dp.toPx(), size.height - 16.dp.toPx())
                                )
                                startAngle += sweep
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(Trans.get("total_expenditure", lang), color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text(formatCurrencyAmount(totalExpense, currency), color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    // Dynamic Color Legend indicators Flow Grid
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        expenseByCategory.forEach { (catName, amt) ->
                            val color = getCategoryColorDynamic(catName, categoriesList)
                            val pct = ((amt / totalExpense) * 100).toInt()
                            Row(
                                modifier = Modifier
                                    .background(LightCardBg, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
                                Text("${Trans.get(catName, lang)} $pct%", color = TextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Quick Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { onAddTransaction(true) }, // Income
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("add_income_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = CashGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground)
                Spacer(modifier = Modifier.width(6.dp))
                Text(Trans.get("new_income", lang), color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
                onClick = { onAddTransaction(false) }, // Expense
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("add_expense_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = ObsidianBackground)
                Spacer(modifier = Modifier.width(6.dp))
                Text(Trans.get("new_expense", lang), color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // History Transactions Label
        Text(
            text = Trans.get("recent_tx", lang),
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DeepCardBg),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(Trans.get("no_tx", lang), color = TextMuted, fontSize = 12.sp)
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                transactions.forEach { tx ->
                    TransactionRow(
                        transaction = tx,
                        categoriesList = categoriesList,
                        lang = lang,
                        currency = currency,
                        onDelete = { onDeleteTransaction(tx) }
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionRow(
    transaction: Transaction,
    categoriesList: List<CategoryEntity>,
    lang: String,
    currency: String,
    onDelete: () -> Unit
) {
    val isIncome = transaction.amount > 0
    val categoryColor = getCategoryColorDynamic(transaction.category, categoriesList)
    val categoryIcon = getCategoryIconDynamic(transaction.category, categoriesList)

    val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val dateStr = formatter.format(Date(transaction.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("transaction_item_${transaction.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DeepCardBg),
        border = BorderStroke(1.dp, LightCardBg)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(categoryColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = categoryIcon,
                        contentDescription = transaction.category,
                        tint = categoryColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = transaction.title,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${Trans.get(transaction.category, lang)} • $dateStr",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    if (transaction.notes.isNotEmpty()) {
                        Text(
                            text = transaction.notes,
                            color = TextMuted,
                            fontSize = 9.sp,
                            style = TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val sign = if (isIncome) "+" else ""
                val color = if (isIncome) CashGreen else DebitRed
                Text(
                    text = "$sign${formatCurrencyAmount(transaction.amount, currency)}",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "حذف", tint = TextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Screen B: Categories Customization
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun CategoriesScreen(
    categories: List<CategoryEntity>,
    lang: String,
    onAddCategoryClick: () -> Unit,
    onDeleteCategory: (CategoryEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(Trans.get("categories", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(Trans.get("category_desc", lang), color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddCategoryClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_category_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(Trans.get("add_category", lang), color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { cat ->
                val color = parseHexColor(cat.colorHex)
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("category_card_${cat.id}"),
                    colors = CardDefaults.cardColors(containerColor = DeepCardBg),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, LightCardBg)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = mapIconNameToVector(cat.iconName),
                                    contentDescription = cat.name,
                                    tint = color,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            if (!cat.isDefault) {
                                IconButton(onClick = { onDeleteCategory(cat) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف الفئة", tint = DebitRed.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(Trans.get(cat.name, lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = if (cat.isIncome) Trans.get("income_term", lang) else Trans.get("expense_term", lang),
                            color = if (cat.isIncome) CashGreen else GoldAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Screen C: Recurring Operations Configurations
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun RecurringScreen(
    recurring: List<RecurringTransaction>,
    categoriesList: List<CategoryEntity>,
    lang: String,
    currency: String,
    onAddRecurringClick: () -> Unit,
    onDeleteRecurring: (RecurringTransaction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(Trans.get("schedule", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(Trans.get("schedule_desc", lang), color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddRecurringClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_recurring_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(Trans.get("add_schedule", lang), color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        if (recurring.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepCardBg),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(Trans.get("no_schedule", lang), color = TextMuted, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(recurring) { rec ->
                    val isIncome = rec.type == "INCOME"
                    val col = getCategoryColorDynamic(rec.category, categoriesList)
                    val icon = getCategoryIconDynamic(rec.category, categoriesList)

                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("recurring_card_${rec.id}"),
                        colors = CardDefaults.cardColors(containerColor = DeepCardBg),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, LightCardBg)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(col.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(icon, contentDescription = null, tint = col, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text(rec.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        text = "${Trans.get("due_on", lang)} ${rec.dayOfMonth} • ${Trans.get(rec.category, lang)}",
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val textCol = if (isIncome) CashGreen else DebitRed
                                Text(
                                    text = "${if (isIncome) "+" else "-"}${formatCurrencyAmount(rec.amount, currency)}",
                                    color = textCol,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                IconButton(onClick = { onDeleteRecurring(rec) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = DebitRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Screen D: Reminders Screen
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun RemindersScreen(
    reminders: List<Reminder>,
    lang: String,
    onAddReminderClick: () -> Unit,
    onToggleReminder: (Reminder) -> Unit,
    onDeleteReminder: (Reminder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(Trans.get("reminders", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(Trans.get("reminders_desc", lang), color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddReminderClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_reminder_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(Trans.get("add_reminder", lang), color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        if (reminders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DeepCardBg),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.NotificationsNone, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(Trans.get("no_reminders", lang), color = TextMuted, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reminders) { rem ->
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("reminder_card_${rem.id}"),
                        colors = CardDefaults.cardColors(containerColor = DeepCardBg),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, LightCardBg)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(GoldAccent.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (rem.type == "DAILY_EXPENSE") Icons.Default.EventNote else Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(rem.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        text = if (rem.type == "DAILY_EXPENSE") {
                                            "Daily at ${rem.timeString}"
                                        } else {
                                            "${Trans.get("due_on", lang)} ${rem.dateDay} ${Trans.get("every_month", lang)} ${rem.timeString}"
                                        },
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Switch(
                                    checked = rem.isEnabled,
                                    onCheckedChange = { onToggleReminder(rem) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = ObsidianBackground,
                                        checkedTrackColor = GoldAccent,
                                        uncheckedThumbColor = TextMuted,
                                        uncheckedTrackColor = LightCardBg
                                    )
                                )
                                IconButton(onClick = { onDeleteReminder(rem) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = DebitRed, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Screen E: Detailed Luxury Settings Screen
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun SettingsScreen(
    lang: String,
    currency: String,
    onLanguageChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onClearAll: () -> Unit
) {
    var showConfirmClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column {
            Text(Trans.get("settings", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(Trans.get("settings_desc", lang), color = TextMuted, fontSize = 11.sp)
        }

        // Section: Select App Language
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepCardBg),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, LightCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(Trans.get("language", lang), color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (lang == "en") GoldAccent else LightCardBg)
                            .clickable { onLanguageChange("en") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("English 🇬🇧", color = if (lang == "en") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (lang == "ar") GoldAccent else LightCardBg)
                            .clickable { onLanguageChange("ar") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("العربية 🇸🇦", color = if (lang == "ar") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Section: Select Primary Currency
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DeepCardBg),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, LightCardBg)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(Trans.get("currency", lang), color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                val currencies = listOf(
                    "USD" to "Dollar ($)",
                    "EUR" to "Euro (€)",
                    "EGP" to "Egyptian Pound (EGP)",
                    "SAR" to "Saudi Riyal (SAR)"
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(110.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(currencies) { (code, label) ->
                        val isSelected = currency == code
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldAccent else LightCardBg)
                                .clickable { onCurrencyChange(code) }
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, color = if (isSelected) ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Danger Action: Clear All Database entries
        Button(
            onClick = { showConfirmClearDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = DebitRed.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp),
            border = BorderStroke(1.dp, DebitRed)
        ) {
            Icon(Icons.Default.DeleteForever, contentDescription = null, tint = DebitRed)
            Spacer(modifier = Modifier.width(8.dp))
            Text(Trans.get("clear_data", lang), color = DebitRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        if (showConfirmClearDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmClearDialog = false },
                containerColor = DeepCardBg,
                title = { Text(Trans.get("clear_data", lang), color = TextWhite, fontWeight = FontWeight.Bold) },
                text = { Text(Trans.get("clear_warning", lang), color = TextMuted) },
                confirmButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = DebitRed),
                        onClick = {
                            onClearAll()
                            showConfirmClearDialog = false
                        }
                    ) {
                        Text(Trans.get("confirm", lang), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(contentColor = TextWhite),
                        onClick = { showConfirmClearDialog = false }
                    ) {
                        Text(Trans.get("cancel", lang))
                    }
                }
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog A: Dialog for Adding Cash Incomes/Expenses
// ---------------------------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    isIncome: Boolean,
    categoriesList: List<CategoryEntity>,
    lang: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, category: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var selectedCategory by remember { 
        mutableStateOf(categoriesList.firstOrNull { it.isIncome == isIncome }?.name ?: "Others") 
    }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCardBg,
        title = { 
            Text(
                text = if (isIncome) Trans.get("new_income", lang) else Trans.get("new_expense", lang),
                color = if (isIncome) CashGreen else GoldAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(Trans.get("title", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldAccent,
                        focusedLabelColor = GoldAccent,
                        cursorColor = GoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text(Trans.get("amount", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldAccent,
                        focusedLabelColor = GoldAccent,
                        cursorColor = GoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Category chooser dropdown / row selection
                Text(Trans.get("category", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesList.filter { it.isIncome == isIncome }.forEach { cat ->
                        val isSelected = selectedCategory == cat.name
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldAccent else LightCardBg)
                                .clickable { selectedCategory = cat.name }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = Trans.get(cat.name, lang),
                                color = if (isSelected) ObsidianBackground else TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(Trans.get("notes", lang)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldAccent,
                        focusedLabelColor = GoldAccent,
                        cursorColor = GoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = GoldAccent),
                onClick = {
                    val amount = amountStr.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && amount > 0) {
                        onConfirm(title, amount, selectedCategory, notes)
                    }
                }
            ) {
                Text(Trans.get("save", lang), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = TextWhite),
                onClick = onDismiss
            ) {
                Text(Trans.get("cancel", lang))
            }
        }
    )
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog B: Dialog for adding recurring scheduled items
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun AddRecurringDialog(
    categoriesList: List<CategoryEntity>,
    lang: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, type: String, category: String, dayOfMonth: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("EXPENSE") } // INCOME or EXPENSE
    var selectedCategory by remember { mutableStateOf(categoriesList.firstOrNull()?.name ?: "Others") }
    var dayOfMonthStr by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCardBg,
        title = { Text(Trans.get("add_schedule", lang), color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(Trans.get("title", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text(Trans.get("amount", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth()
                )

                // Type select Income / Expense
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "INCOME") CashGreen else LightCardBg)
                            .clickable { type = "INCOME" }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("income_term", lang), color = if (type == "INCOME") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "EXPENSE") GoldAccent else LightCardBg)
                            .clickable { type = "EXPENSE" }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("expense_term", lang), color = if (type == "EXPENSE") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                OutlinedTextField(
                    value = dayOfMonthStr,
                    onValueChange = { dayOfMonthStr = it },
                    label = { Text(Trans.get("day", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth()
                )

                // Categories selection
                Text(Trans.get("category", lang), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categoriesList.filter { it.isIncome == (type == "INCOME") }.forEach { cat ->
                        val isSelected = selectedCategory == cat.name
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldAccent else LightCardBg)
                                .clickable { selectedCategory = cat.name }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(Trans.get(cat.name, lang), color = if (isSelected) ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountStr.toDoubleOrNull() ?: 0.0
                    val day = dayOfMonthStr.toIntOrNull() ?: 1
                    if (title.isNotEmpty() && amount > 0) {
                        onConfirm(title, amount, type, selectedCategory, day)
                    }
                }
            ) {
                Text(Trans.get("save", lang), color = GoldAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Trans.get("cancel", lang), color = TextWhite)
            }
        }
    )
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog C: Dialog for adding categories
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun AddCategoryDialog(
    lang: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, isIncome: Boolean, iconName: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf("Category") }
    var selectedColor by remember { mutableStateOf("#3B82F6") } // Default Blue

    val icons = listOf(
        "Restaurant", "DirectionsCar", "Receipt", "SportsEsports", "ShoppingBag",
        "MedicalServices", "Home", "Build", "CardGiftcard", "Lightbulb", "Work"
    )
    val colors = listOf(
        "#EF4444", "#F59E0B", "#10B981", "#14B8A6", "#3B82F6", "#8B5CF6", "#EC4899", "#6B7280"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCardBg,
        title = { Text(Trans.get("add_category", lang), color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(Trans.get("title", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isIncome) CashGreen else LightCardBg)
                            .clickable { isIncome = true }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("income_term", lang), color = if (isIncome) ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isIncome) GoldAccent else LightCardBg)
                            .clickable { isIncome = false }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("expense_term", lang), color = if (!isIncome) ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                // Color Selection row
                Text("Select Color", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { c ->
                        val col = parseHexColor(c)
                        val isSelected = selectedColor == c
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(if (isSelected) 2.dp else 0.dp, TextWhite, CircleShape)
                                .clickable { selectedColor = c }
                        )
                    }
                }

                // Icon selection row
                Text("Select Icon", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    icons.forEach { iconName ->
                        val isSelected = selectedIcon == iconName
                        IconButton(
                            onClick = { selectedIcon = iconName },
                            modifier = Modifier
                                .background(if (isSelected) GoldAccent else LightCardBg, RoundedCornerShape(8.dp))
                        ) {
                            Icon(
                                imageVector = mapIconNameToVector(iconName),
                                contentDescription = null,
                                tint = if (isSelected) ObsidianBackground else TextWhite
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotEmpty()) {
                        onConfirm(name, isIncome, selectedIcon, selectedColor)
                    }
                }
            ) {
                Text(Trans.get("save", lang), color = GoldAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Trans.get("cancel", lang), color = TextWhite)
            }
        }
    )
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog D: Dialog for adding billing and daily reminders
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun AddReminderDialog(
    lang: String,
    onDismiss: () -> Unit,
    onConfirm: (title: String, timeString: String, type: String, dateDay: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("09") }
    var min by remember { mutableStateOf("00") }
    var type by remember { mutableStateOf("DAILY_EXPENSE") } // DAILY_EXPENSE or BILL_PAYMENT
    var dayOfMonth by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DeepCardBg,
        title = { Text(Trans.get("add_reminder", lang), color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(Trans.get("title", lang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "DAILY_EXPENSE") GoldAccent else LightCardBg)
                            .clickable { type = "DAILY_EXPENSE" }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("daily_exp", lang), color = if (type == "DAILY_EXPENSE") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (type == "BILL_PAYMENT") GoldAccent else LightCardBg)
                            .clickable { type = "BILL_PAYMENT" }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Trans.get("bill_pay", lang), color = if (type == "BILL_PAYMENT") ObsidianBackground else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }

                if (type == "BILL_PAYMENT") {
                    OutlinedTextField(
                        value = dayOfMonth,
                        onValueChange = { dayOfMonth = it },
                        label = { Text(Trans.get("day", lang)) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = hour,
                        onValueChange = { hour = it },
                        label = { Text("Hour") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                        modifier = Modifier.weight(1f)
                    )
                    Text(":", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    OutlinedTextField(
                        value = min,
                        onValueChange = { min = it },
                        label = { Text("Minute") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedLabelColor = GoldAccent),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val day = dayOfMonth.toIntOrNull() ?: 1
                    if (title.isNotEmpty()) {
                        onConfirm(title, "$hour:$min", type, day)
                    }
                }
            ) {
                Text(Trans.get("save", lang), color = GoldAccent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(Trans.get("cancel", lang), color = TextWhite)
            }
        }
    )
}
