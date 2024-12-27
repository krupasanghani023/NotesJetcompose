package com.note.compose.appwrite

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.note.compose.R
import com.note.compose.appwrite.model.RentalData
import com.note.compose.appwrite.ui.theme.ComposeTheme
import com.note.compose.appwrite.util.AddEditTenantScreen
import com.note.compose.appwrite.util.Property_Id
import com.note.compose.appwrite.util.RentalFormUI
import com.note.compose.appwrite.util.TenantListScreen
import com.note.compose.appwrite.util.Tenants_Id
import com.note.compose.appwrite.util.databaseId
import com.note.compose.appwrite.viewmodel.AllocatedViewModel
import com.note.compose.appwrite.viewmodel.MainViewModel
import com.note.compose.appwrite.viewmodel.ResultState
import com.note.compose.appwrite.viewmodel.TenantsViewModel

class TenantsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppwriteClient.init(applicationContext)
        val database = AppwriteClient.database

        val tenantsViewModel = TenantsViewModel(
            database = database,
            databaseId = databaseId,
            TenantsCollectionID = Tenants_Id
        )
        val propertyViewModel = MainViewModel(
            database = database,
            databaseId = databaseId,
            collectionId = Property_Id
        )
        val allocatedViewModel=AllocatedViewModel(
            database = database,
            databaseId = databaseId)
        setContent {
            ComposeTheme {
                MainScreen(
                    propertyViewModel = propertyViewModel,
                    tenantsViewModel = tenantsViewModel,
                    allocatedViewModel=allocatedViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(propertyViewModel: MainViewModel, tenantsViewModel: TenantsViewModel,
               allocatedViewModel: AllocatedViewModel) {
    val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("Property") }
    var showTenantOptions by remember { mutableStateOf(false) }
    val currentPropertyItem by propertyViewModel.currentItem.collectAsState()
    val currentTenantItem by tenantsViewModel.currentItem.collectAsState()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_07011c),
                    titleContentColor = colorResource(id = R.color.white),
                ),
                title = {
                    // Change title dynamically based on the current route
                    Text(
                        text = when (currentRoute) {
                            "add_edit_property" -> {
                                if (currentPropertyItem == null) {
                                    "Add Property"
                                } else {
                                    "Edit Property"
                                }
                            }

                            "add_edit_tenant" -> {
                                if (currentTenantItem == null) {
                                    "Add Tenant"
                                } else {
                                    "Edit Tenant"
                                }
                            }

                            "add_edit_allocation" -> "Allocation"
                            else -> if (selectedTab.value == "Property") "Property List" else "Tenants List"
                        },
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                            ),
                            fontSize = 30.sp,
                        ),
                    )
                },
                navigationIcon = {
                    // Show back button only on add/edit screens
                    if (currentRoute in listOf(
                            "add_edit_property",
                            "add_edit_tenant",
                            "add_edit_allocation"
                        )
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = colorResource(id = R.color.white)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentRoute !in listOf(
                    "add_edit_property",
                    "add_edit_tenant",
                    "add_edit_allocation"
                )
            ) {

                FloatingActionButton(
                    onClick = {
                        when (selectedTab.value) {
                            "Property" -> {
                                navController.navigate("add_edit_property")
                                propertyViewModel.setCurrentItem(null)
                            }

                            "Tenants" -> {
                                showTenantOptions = true
                                tenantsViewModel.setCurrentItem(null)
                            }
                        }
                    },
                    shape = CircleShape,
                    containerColor = colorResource(id = R.color.white),
                    modifier = Modifier
                        .size(56.dp)
                        .offset(y = 50.dp),
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            // Hide bottom navigation when on Add/Edit screens
            if (currentRoute !in listOf(
                    "add_edit_property",
                    "add_edit_tenant",
                    "add_edit_allocation"
                )
            ) {
                BottomNavigation(
                    backgroundColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .background(
                            colorResource(id = R.color.white),
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                ) {
                    BottomNavigationItem(
                        selected = selectedTab.value == "Property",
                        onClick = {
                            selectedTab.value = "Property"
                            navController.navigate("property_list") {
                                popUpTo("property_list") { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Property"
                            )
                        },
                        label = { Text("Property") }
                    )
                    BottomNavigationItem(
                        selected = selectedTab.value == "Tenants",
                        onClick = {
                            selectedTab.value = "Tenants"
                            navController.navigate("tenant_list") {
                                popUpTo("tenant_list") { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Tenants"
                            )
                        },
                        label = { Text("Tenants") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = "property_list"
            ) {
                composable("property_list") {
                    PropertyScreen(navController = navController, propertyViewModel)
                }
                composable("tenant_list") {
                    TenantListScreen(navController, tenantsViewModel)
                }
                composable("add_edit_property") {
                    AddEditPropertyScreen(
                        onSave = { navController.popBackStack() },
                        viewModel = propertyViewModel
                    )
                }
                composable("add_edit_tenant") {
                    AddEditTenantScreen(
                        onSave = { navController.popBackStack() },
                        tenantsViewModel = tenantsViewModel
                    )
                }
                composable("add_edit_allocation") {
                    AddEditAllocationScreen(
                        onSave = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() },
                        propertyViewModel = propertyViewModel,
                        tenantsViewModel=tenantsViewModel,
                        allocatedViewModel=allocatedViewModel
                    )
                }
            }

            if (showTenantOptions) {
                TenantOptionsMenu(
                    onAddTenant = {
                        showTenantOptions = false
                        navController.navigate("add_edit_tenant")
                    },
                    onAllocateTenant = {
                        showTenantOptions = false
                        navController.navigate("add_edit_allocation")
                    },
                    onClose = { showTenantOptions = false }
                )
            }
        }
    }
}


@Composable
fun PropertyScreen(navController: NavController, viewModel: MainViewModel) {
    PropertyListScreen(
        viewModel = viewModel,
        onEditItem = {
            viewModel.setCurrentItem(it)
            navController.navigate("add_edit_property")

        }
    )
}

@Composable
fun PropertyListScreen(
    viewModel: MainViewModel,
    onEditItem: (RentalData) -> Unit,
) {

    val state by viewModel.state.collectAsState()

    var items by remember { mutableStateOf<List<RentalData>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<String?>(null) }
    val view = LocalView.current
    val window = (view.context as Activity).window

    WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = false


    LaunchedEffect(Unit) {
        viewModel.fetchItems()
    }

    when (state) {
        is ResultState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Take full screen space
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center), // Center the indicator
                    color = MaterialTheme.colorScheme.primary, // Use theme color or custom color
                    strokeWidth = 4.dp // Optional: Adjust stroke width
                )
            }
        }

        is ResultState.Success -> {
            items = (state as ResultState.Success<List<RentalData>>).data

        }

        is ResultState.Error -> {
            val error = (state as ResultState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "No Property Yet!",
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                        ),
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(items) { item ->
                    RentItemRow(
                        item,
                        onEdit = { onEditItem(item) },
                        onDelete = {
                            itemToDelete = it
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }

        if (showDeleteDialog) {
            ConfirmDeleteDialog(
                onConfirm = {
                    itemToDelete?.let { viewModel.deleteItem(it) }
                    showDeleteDialog = false
                },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }

}


@Composable
fun AddEditPropertyScreen(onSave: () -> Unit, viewModel: MainViewModel) {


    val currentItem by viewModel.currentItem.collectAsState()
    Log.d("MyTesting", "CurrentItem:$currentItem")

    var name by remember { mutableStateOf(currentItem?.name ?: "") }
    var address by remember { mutableStateOf(currentItem?.address ?: "") }
    var rentAmount by remember { mutableStateOf(currentItem?.rentAmount?.toString() ?: "") }
    var advanceAmount by remember { mutableStateOf(currentItem?.advanceAmount?.toString() ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var rentAmountError by remember { mutableStateOf(false) }
    var advanceAmountError by remember { mutableStateOf(false) }
    var advanceAmountOverError by remember { mutableStateOf(false) }
    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val rainbowColors: List<Color> = listOf(
                    colorResource(id = R.color.color_D81B60), colorResource(
                        id = R.color.color_5E35B1
                    ), colorResource(id = R.color.color_00ACC1)
                )
                val brush = remember {
                    Brush.linearGradient(
                        colors = rainbowColors
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false // Reset error when user starts typing
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.name),
                            color = colorResource(id = R.color.color_979797),
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_regular, FontWeight.Normal)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontSize = 17.sp,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = nameError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
                    )

                )
                if (nameError) {
                    Text(
                        text = stringResource(id = R.string.name_is_required),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = false
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.address),
                            color = colorResource(id = R.color.color_979797),
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_regular, FontWeight.Normal)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontSize = 17.sp,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = addressError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
                    )
                )
                if (addressError) {
                    Text(
                        text = stringResource(id = R.string.address_is_required),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }

                OutlinedTextField(
                    value = rentAmount,
                    onValueChange = {
                        rentAmount = it
                        rentAmountError = false
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.rent_amount),
                            color = colorResource(id = R.color.color_979797),
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_regular, FontWeight.Normal)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontSize = 17.sp,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = rentAmountError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
                    )
                )
                if (rentAmountError) {
                    Text(
                        text = stringResource(id = R.string.rent_amount_is_required),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }

                OutlinedTextField(
                    value = advanceAmount,
                    onValueChange = {
                        val rentValue = rentAmount.toIntOrNull() ?: 0
                        val advanceValue = it.toIntOrNull() ?: 0

                        if (advanceValue <= rentValue) {
                            advanceAmount = it
                            advanceAmountOverError = false
                        } else {
                            advanceAmountOverError = true
                        }
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.advance_amount),
                            color = colorResource(id = R.color.color_979797),
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_regular, FontWeight.Normal)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.black),
                        fontSize = 17.sp,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    isError = advanceAmountError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c),
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
                    )
                )
                if (advanceAmountOverError) {
                    Text(
                        text = stringResource(id = R.string.advance_amount_cannot_exceed_rent_amount),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }

                if (advanceAmountError) {
                    Text(
                        text = stringResource(id = R.string.advance_amount_is_required),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Button(
                        onClick = {
                            // Save the item (add or edit)
                            // Validate inputs
                            nameError = name.isBlank()
                            addressError = address.isBlank()
                            rentAmountError = rentAmount.isBlank()
                            advanceAmountError = advanceAmount.isBlank()

                            // If no errors, save the data
                            if (!nameError && !addressError && !rentAmountError && !advanceAmountError) {

                                viewModel.saveItem(
                                    RentalData(
                                        id = currentItem?.id ?: "",
                                        name = name,
                                        address = address,
                                        rentAmount = rentAmount.toIntOrNull() ?: 0,
                                        advanceAmount = advanceAmount.toIntOrNull() ?: 0
                                    )
                                )
                                onSave()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.color_07011c), // Custom background color
                            contentColor = colorResource(id = R.color.white) // Custom text/icon color
                        )
                    ) {
                        Text(
                            stringResource(id = R.string.save), modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp), textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                                ),
                                fontSize = 28.sp,
                            )
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun AddEditAllocationScreen(onSave: () -> Unit,
                            onCancel: () -> Unit,
                            propertyViewModel: MainViewModel,
                            tenantsViewModel: TenantsViewModel,
                            allocatedViewModel: AllocatedViewModel) {
    RentalFormUI(
        onSave = onSave,
        onCancel = onCancel,
        propertyViewModel = propertyViewModel,
        tenantsViewModel=tenantsViewModel,
    allocatedViewModel=allocatedViewModel)
}

@Composable
fun TenantOptionsMenu(
    onAddTenant: () -> Unit,
    onAllocateTenant: () -> Unit,
    onClose: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(color = Color(0x80000000)) // Transparent black background to dim the content
            .clickable { onClose() }, // Close menu when clicking outside
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(26.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(6.dp)
            ) {
                Text(
                    text = "Add Tenant",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                        .clickable {
                            // Action for "Add Tenant"
                            onAddTenant()
                        }
                )
                Text(
                    text = "Allocate Tenant",
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                        .clickable {
                            // Action for "Allocate Tenant"
                            onAllocateTenant()
                        }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        MainScreen(viewModel(), viewModel(), viewModel())
    }
}