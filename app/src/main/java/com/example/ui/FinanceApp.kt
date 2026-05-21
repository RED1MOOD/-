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

// Dynamic custom utility functions for Category properties mapping
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
        "طعام" -> Color(0xFFF59E0B)
        "مواصلات" -> Color(0xFF14B8A6)
        "فواتير" -> Color(0xFF3B82F6)
        "ترفيه" -> Color(0xFFEC4899)
        "ملابس" -> Color(0xFF8B5CF6)
        "أدوية" -> Color(0xFFEF4444)
        "راتب" -> Color(0xFF10B981)
        "إستثمار" -> Color(0xFF059669)
        "قط/منحة" -> Color(0xFFEAB308)
        else -> Color(0xFF6B7280)
    }
}

fun getCategoryIcon(categoryName: String): ImageVector {
    return when (categoryName) {
        "طعام" -> Icons.Default.Restaurant
        "مواصلات" -> Icons.Default.DirectionsCar
        "فواتير" -> Icons.Default.Receipt
        "ترفيه" -> Icons.Default.SportsEsports
        "ملابس" -> Icons.Default.ShoppingBag
        "أدوية" -> Icons.Default.MedicalServices
        "راتب" -> Icons.Default.Work
        "إستثمار" -> Icons.Default.TrendingUp
        "قط/منحة" -> Icons.Default.CardGiftcard
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

    var showAddTxDialog by remember { mutableStateOf(false) }
    var dialogIsIncome by remember { mutableStateOf(false) }
    var showAddRecDialog by remember { mutableStateOf(false) }
    var showAddCatDialog by remember { mutableStateOf(false) }
    var showAddRemDialog by remember { mutableStateOf(false) }

    // Enforce Arabic RTL Layout Direction globally across the screen
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
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
                            icon = { Icon(Icons.Default.Dashboard, contentDescription = "الرئيسية") },
                            label = { Text("الرئيسية", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                            icon = { Icon(Icons.Default.Category, contentDescription = "التصنيفات") },
                            label = { Text("التصنيفات", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                            icon = { Icon(Icons.Default.Autorenew, contentDescription = "المعاملات الدورية") },
                            label = { Text("المجدولة", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                            icon = { Icon(Icons.Default.NotificationsActive, contentDescription = "التنبيهات") },
                            label = { Text("التنبيهات", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                            onAddTransaction = { isIncome ->
                                dialogIsIncome = isIncome
                                showAddTxDialog = true
                            },
                            onDeleteTransaction = { viewModel.deleteTransaction(it) },
                            onClearAll = { viewModel.clearAllTransactions() }
                        )
                        1 -> CategoriesScreen(
                            categories = categories,
                            onAddCategoryClick = { showAddCatDialog = true },
                            onDeleteCategory = { viewModel.deleteCategory(it) }
                        )
                        2 -> RecurringScreen(
                            recurring = recurring,
                            categoriesList = categories,
                            onAddRecurringClick = { showAddRecDialog = true },
                            onDeleteRecurring = { viewModel.deleteRecurringTransaction(it) }
                        )
                        3 -> RemindersScreen(
                            reminders = reminders,
                            onAddReminderClick = { showAddRemDialog = true },
                            onToggleReminder = { viewModel.toggleReminderEnabled(it) },
                            onDeleteReminder = { viewModel.deleteReminder(it) }
                        )
                    }

                    // Dialog Trigger Logic
                    if (showAddTxDialog) {
                        AddTransactionDialog(
                            isIncome = dialogIsIncome,
                            categoriesList = categories,
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
                            onDismiss = { showAddRecDialog = false },
                            onConfirm = { title, amount, type, category, dayOfMonth ->
                                viewModel.addRecurringTransaction(title, amount, type, category, dayOfMonth)
                                showAddRecDialog = false
                            }
                        )
                    }

                    if (showAddCatDialog) {
                        AddCategoryDialog(
                            onDismiss = { showAddCatDialog = false },
                            onConfirm = { name, isIncome, icon, color ->
                                viewModel.addCategory(name, isIncome, icon, color)
                                showAddCatDialog = false
                            }
                        )
                    }

                    if (showAddRemDialog) {
                        AddReminderDialog(
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
    onAddTransaction: (Boolean) -> Unit,
    onDeleteTransaction: (Transaction) -> Unit,
    onClearAll: () -> Unit
) {
    val df = DecimalFormat("#,###.##")
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
                    text = "مرحباً بك في حساباتي 👋",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "أدر وتتبع أموالك الخاصة بكل يسر وسهولة",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            IconButton(
                onClick = onClearAll,
                modifier = Modifier
                    .background(LightCardBg, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = "مسح كافة البيانات",
                    tint = DebitRed,
                    modifier = Modifier.size(20.dp)
                )
            }
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
                    Text("الرصيد الكلي الحالي", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${df.format(balance)} ج.م",
                        color = if (balance >= 0) CashGreen else DebitRed,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
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
                        Text("الواردات (+)", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${df.format(totalIncome)} ج.م", color = CashGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                        Text("المصروفات (-)", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("${df.format(totalExpense)} ج.م", color = DebitRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                        text = "تفصيل بياني للمصروفات حسب الفئة",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Right,
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
                            Text("إجمالي النفقات", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("${df.format(totalExpense)}", color = TextWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
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
                                Text("$catName $pct%", color = TextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
                Text("وارد جديد", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
                Text("مصروف جديد", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // History Transactions Label
        Text(
            text = "آخر المعاملات والعمليات المسجلة",
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
                    Text("لا توجد عمليات مسجلة حالياً.", color = TextMuted, fontSize = 12.sp)
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
                        text = "${transaction.category} • $dateStr",
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
                    text = "$sign${DecimalFormat("#,###.##").format(transaction.amount)} ج.م",
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
                Text("إدارة تصنيفات الأموال 📁", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("تصنيفات الدخل والمصاريف المخصصة لتنظيم عملياتك", color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddCategoryClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_category_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("إضافة فئة", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories.size) { index ->
                val cat = categories[index]
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
                        Text(cat.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            text = if (cat.isIncome) "فوائد/واردات (+)" else "مصروفات دافعة (-)",
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
                Text("المعاملات والتكرارات الدورية 🔄", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("الأقساط الشهرية، الرواتب والاشتراكات المقيدة آلياً", color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddRecurringClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_recurring_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("جدولة معاملة", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                    Text("لا يوجد قيود مجدولة شهرياً حالياً.", color = TextMuted, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(recurring.size) { index ->
                    val rec = recurring[index]
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
                                        text = "يوم ${rec.dayOfMonth} في الشهر الدروي • ${rec.category}",
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
                                    text = "${if (isIncome) "+" else "-"}${DecimalFormat("#,###.##").format(rec.amount)} ج.م",
                                    color = textCol,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                IconButton(onClick = { onDeleteRecurring(rec) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "حذف الجدولة", tint = DebitRed, modifier = Modifier.size(16.dp))
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
// Screen D: Reminders and Billing Alarms Management
// ---------------------------------------------------------------------------------------------------------------------
@Composable
fun RemindersScreen(
    reminders: List<Reminder>,
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
                Text("منبهات الاستقصاء والتحصيل 🔔", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("تذكير بخصوص تعقب وتسجيل المصروفات اليومية والفواتير", color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onAddReminderClick,
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("add_reminder_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("تذكير جديد", color = ObsidianBackground, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                    Text("لا توجد تذكيرات مميزة حالياً.", color = TextMuted, fontSize = 11.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reminders.size) { index ->
                    val rem = reminders[index]
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
                                            "تنبيه يومي آلي الساعة ${rem.timeString}"
                                        } else {
                                            "فاتورة شهرية يوم ${rem.dateDay} في الشهر الساعة ${rem.timeString}"
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
                                    Icon(Icons.Default.Delete, contentDescription = "حذف التنبية", tint = DebitRed, modifier = Modifier.size(16.dp))
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
// Dialog A: Dialog for Adding Cash Incomes/Expenses
// ---------------------------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    isIncome: Boolean,
    categoriesList: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, category: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val filteredCats = categoriesList.filter { it.isIncome == isIncome }

    LaunchedEffect(isIncome, categoriesList) {
        selectedCategory = if (filteredCats.isNotEmpty()) filteredCats.first().name else "أخرى"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("add_transaction_dialog"),
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountStr.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && amt > 0) {
                        onConfirm(title, amt, selectedCategory, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = if (isIncome) CashGreen else GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("إدخال المعاملة", color = ObsidianBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextMuted)
            }
        },
        title = {
            Text(
                text = if (isIncome) "إضافة عملية وارد مالي (+)" else "تسجيل فاتورة صادر ومصروف (-)",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = DeepCardBg,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان العملية (مثال: راتب، طلب دليفري)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = if (isIncome) CashGreen else GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("المبلغ المالي (ج.م)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = if (isIncome) CashGreen else GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = if (isIncome) CashGreen else GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                Text("اختر التصنيف المالي", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                ) {
                    filteredCats.forEach { cat ->
                        val isSelected = selectedCategory == cat.name
                        val color = parseHexColor(cat.colorHex)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat.name },
                            label = { Text(cat.name, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (isIncome) CashGreen else GoldAccent,
                                selectedLabelColor = ObsidianBackground
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = mapIconNameToVector(cat.iconName),
                                    contentDescription = cat.name,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isSelected) ObsidianBackground else color
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog B: Dialog for adding customised recurring/config schedules
// ---------------------------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecurringDialog(
    categoriesList: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, amount: Double, type: String, category: String, dayOfMonth: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var recurringType by remember { mutableStateOf("EXPENSE") } // "INCOME" or "EXPENSE"
    var selectedCategory by remember { mutableStateOf("") }
    var dayOfMonth by remember { mutableStateOf(1) }

    val filteredCats = categoriesList.filter { it.isIncome == (recurringType == "INCOME") }

    LaunchedEffect(recurringType, categoriesList) {
        selectedCategory = if (filteredCats.isNotEmpty()) filteredCats.first().name else "أخرى"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("add_recurring_dialog"),
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountStr.toDoubleOrNull() ?: 0.0
                    if (title.isNotEmpty() && amt > 0) {
                        onConfirm(title, amt, recurringType, selectedCategory, dayOfMonth)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("حفظ المعاملة الدورية", color = ObsidianBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextMuted)
            }
        },
        title = {
            Text(
                text = "جدولة عملية مستمرة تلقائياً",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = DeepCardBg,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان العملية الدورية (قسط السيارة، الفايبر الافتراضي)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("المبلغ المالي (ج.م)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("نوع العملية المستمرة", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { recurringType = "EXPENSE" },
                            shape = RoundedCornerShape(8.dp),
                            color = if (recurringType == "EXPENSE") DebitRed.copy(alpha = 0.2f) else LightCardBg,
                            border = BorderStroke(1.dp, if (recurringType == "EXPENSE") DebitRed else Color.Transparent)
                        ) {
                            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                Text("مصروف متكرر (-)", color = if (recurringType == "EXPENSE") DebitRed else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { recurringType = "INCOME" },
                            shape = RoundedCornerShape(8.dp),
                            color = if (recurringType == "INCOME") CashGreen.copy(alpha = 0.2f) else LightCardBg,
                            border = BorderStroke(1.dp, if (recurringType == "INCOME") CashGreen else Color.Transparent)
                        ) {
                            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                Text("دخل متكرر (+)", color = if (recurringType == "INCOME") CashGreen else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Text("يوم التنفيذ الشهري من كل شهر", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (dayOfMonth > 1) dayOfMonth-- }, modifier = Modifier.background(LightCardBg, CircleShape).size(36.dp)) {
                        Icon(Icons.Default.Remove, null, tint = TextWhite)
                    }
                    Text("يوم $dayOfMonth في الشهر", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    IconButton(onClick = { if (dayOfMonth < 31) dayOfMonth++ }, modifier = Modifier.background(LightCardBg, CircleShape).size(36.dp)) {
                        Icon(Icons.Default.Add, null, tint = TextWhite)
                    }
                }

                Text("اختر التصنيف المالي", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                ) {
                    filteredCats.forEach { cat ->
                        val isSelected = selectedCategory == cat.name
                        val color = parseHexColor(cat.colorHex)
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat.name },
                            label = { Text(cat.name, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (recurringType == "INCOME") CashGreen else GoldAccent,
                                selectedLabelColor = ObsidianBackground
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = mapIconNameToVector(cat.iconName),
                                    contentDescription = cat.name,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isSelected) ObsidianBackground else color
                                )
                            }
                        )
                    }
                }
            }
        }
    )
}

// Preset color constants for new category insertions
val CATEGORY_PRESETS_COLORS = listOf(
    "#F59E0B", // Amber Preset
    "#10B981", // Emerald Preset
    "#3B82F6", // Blue Preset
    "#8B5CF6", // Purple Preset
    "#EC4899", // Pink Preset
    "#14B8A6", // Teal Preset
    "#F43F5E", // Rose Preset
    "#FF5722"  // Coral Preset
)

// Preset icon pairing mappings
val CATEGORY_PRESETS_ICONS = listOf(
    "Restaurant" to Icons.Default.Restaurant,
    "DirectionsCar" to Icons.Default.DirectionsCar,
    "Receipt" to Icons.Default.Receipt,
    "SportsEsports" to Icons.Default.SportsEsports,
    "ShoppingBag" to Icons.Default.ShoppingBag,
    "MedicalServices" to Icons.Default.MedicalServices,
    "School" to Icons.Default.School,
    "FitnessCenter" to Icons.Default.FitnessCenter,
    "Home" to Icons.Default.Home,
    "Build" to Icons.Default.Build,
    "CardGiftcard" to Icons.Default.CardGiftcard,
    "Lightbulb" to Icons.Default.Lightbulb
)

// ---------------------------------------------------------------------------------------------------------------------
// Dialog C: Dialog for creating dynamic custom categories
// ---------------------------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, isIncome: Boolean, iconName: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(CATEGORY_PRESETS_COLORS.first()) }
    var selectedIcon by remember { mutableStateOf(CATEGORY_PRESETS_ICONS.first().first) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("add_category_dialog"),
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        onConfirm(name, isIncome, selectedIcon, selectedColor)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("تأكيد وحفظ", color = ObsidianBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextMuted)
            }
        },
        title = {
            Text(
                text = "إنشاء تصنيف ومجوعة جديدة",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = DeepCardBg,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم التصنيف (مثال: مستودع، أكل قطط)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isIncome = false },
                        shape = RoundedCornerShape(8.dp),
                        color = if (!isIncome) DebitRed.copy(alpha = 0.2f) else LightCardBg,
                        border = BorderStroke(1.dp, if (!isIncome) DebitRed else Color.Transparent)
                    ) {
                        Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                            Text("مصروفات (-)", color = if (!isIncome) DebitRed else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { isIncome = true },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isIncome) CashGreen.copy(alpha = 0.2f) else LightCardBg,
                        border = BorderStroke(1.dp, if (isIncome) CashGreen else Color.Transparent)
                    ) {
                        Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                            Text("واردات (+)", color = if (isIncome) CashGreen else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }

                Text("اختر لوناً للتصنيف", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End)
                ) {
                    CATEGORY_PRESETS_COLORS.forEach { hex ->
                        val col = parseHexColor(hex)
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(col)
                                .border(
                                    2.dp,
                                    if (selectedColor == hex) TextWhite else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Text("اختر رمزاً للتصنيف", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                ) {
                    CATEGORY_PRESETS_ICONS.forEach { (iconKey, vector) ->
                        val isSelected = selectedIcon == iconKey
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GoldAccent else LightCardBg)
                                .clickable { selectedIcon = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = vector,
                                contentDescription = null,
                                tint = if (isSelected) ObsidianBackground else TextWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

// ---------------------------------------------------------------------------------------------------------------------
// Dialog D: Dialog for adding customized user reminders/config schedules
// ---------------------------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, timeString: String, type: String, dateDay: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf(9) }
    var minute by remember { mutableStateOf(0) }
    var type by remember { mutableStateOf("DAILY_EXPENSE") } // "DAILY_EXPENSE" or "BILL_PAYMENT"
    var dateDay by remember { mutableStateOf(1) } // for bill payment

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("add_reminder_dialog"),
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotEmpty()) {
                        val paddedHour = hour.toString().padStart(2, '0')
                        val paddedMinute = minute.toString().padStart(2, '0')
                        onConfirm(title, "$paddedHour:$paddedMinute", type, dateDay)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("حفظ التنبيه", color = ObsidianBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextMuted)
            }
        },
        title = {
            Text(
                text = "ضبط وإضافة تذكير مالي مميز",
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = DeepCardBg,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان التذكير (مثلاً: فاتورة النت، تسجيل المصاريف اليومية)") },
                    textStyle = TextStyle(textAlign = TextAlign.Right, color = TextWhite),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedLabelColor = GoldAccent,
                        unfocusedLabelColor = TextMuted
                    )
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("نوع التنبيه المالي", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { type = "DAILY_EXPENSE" },
                            shape = RoundedCornerShape(8.dp),
                            color = if (type == "DAILY_EXPENSE") GoldAccent.copy(alpha = 0.2f) else LightCardBg,
                            border = BorderStroke(1.dp, if (type == "DAILY_EXPENSE") GoldAccent else Color.Transparent)
                        ) {
                            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                Text("مصروفات يومية", color = if (type == "DAILY_EXPENSE") GoldAccent else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { type = "BILL_PAYMENT" },
                            shape = RoundedCornerShape(8.dp),
                            color = if (type == "BILL_PAYMENT") GoldAccent.copy(alpha = 0.2f) else LightCardBg,
                            border = BorderStroke(1.dp, if (type == "BILL_PAYMENT") GoldAccent else Color.Transparent)
                        ) {
                            Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center) {
                                Text("دفع الفواتير", color = if (type == "BILL_PAYMENT") GoldAccent else TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }

                if (type == "BILL_PAYMENT") {
                    Text("يوم سداد الفاتورة تلقائياً من كل شهر", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { if (dateDay > 1) dateDay-- }, modifier = Modifier.background(LightCardBg, CircleShape).size(36.dp)) {
                            Icon(Icons.Default.Remove, null, tint = TextWhite)
                        }
                        Text("يوم $dateDay في الشهر", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        IconButton(onClick = { if (dateDay < 31) dateDay++ }, modifier = Modifier.background(LightCardBg, CircleShape).size(36.dp)) {
                            Icon(Icons.Default.Add, null, tint = TextWhite)
                        }
                    }
                }

                Text("توقيت الاستفسار (منبه الدفع)", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("الدقيقة", color = TextMuted, fontSize = 9.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { minute = (minute + 5) % 60 }, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.KeyboardArrowUp, null, tint = TextMuted)
                            }
                            Text(minute.toString().padStart(2, '0'), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            IconButton(onClick = { minute = (minute + 55) % 60 }, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.KeyboardArrowDown, null, tint = TextMuted)
                            }
                        }
                    }

                    Text(":", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 24.sp)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("الساعة", color = TextMuted, fontSize = 9.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { hour = (hour + 1) % 24 }, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.KeyboardArrowUp, null, tint = TextMuted)
                            }
                            Text(hour.toString().padStart(2, '0'), color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            IconButton(onClick = { hour = (hour + 23) % 24 }, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.KeyboardArrowDown, null, tint = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    )
}
