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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.note.compose.appwrite.util.AddEditAllocationScreen
import com.note.compose.appwrite.util.AddEditTenantScreen
import com.note.compose.appwrite.util.Property_Id
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
    // Update selected tab based on current route
    LaunchedEffect(currentRoute) {
        selectedTab.value = when (currentRoute) {
            "property_list" -> "Property"
            "tenant_list" -> "Tenants"
            else -> "Property" // Default to Property tab
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_926C57),
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
                                Font(R.font.karla_bold)
                            ),
                            fontSize = 23.sp,
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
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        modifier = Modifier.size(20.dp), // Adjust the icon size
                        tint = colorResource(id = R.color.black),
                        contentDescription = "Add")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,

        bottomBar = {
            // Hide bottom navigation when on Add/Edit screens
            if (currentRoute !in listOf("add_edit_property", "add_edit_tenant", "add_edit_allocation")) {
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
                                contentDescription = "Property",
                                tint = if(selectedTab.value=="Property"){

                                    colorResource(id = R.color.black)}else{
                                    colorResource(id = R.color.gray_400)}
                            )
                        },
                        label = { Text("Property", style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.karla_medium)
                            ),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ), color = if(selectedTab.value=="Property"){
                            colorResource(id = R.color.black)}else{
                            colorResource(id = R.color.gray_400)}) },
                        alwaysShowLabel = true
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
                                contentDescription = "Tenants",
                                tint = if(selectedTab.value=="Tenants"){
                                    colorResource(id = R.color.black)}else{
                                    colorResource(id = R.color.gray_400)}
                            )
                        },
                        label = { Text("Tenants", style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.karla_semi_bold)
                            ),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ), color = if(selectedTab.value=="Tenants"){
                            colorResource(id = R.color.black)}else{
                            colorResource(id = R.color.gray_400)}) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .background(colorResource(id = R.color.white))) {
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
    var showProgressIndicator by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<String?>(null) }
    val view = LocalView.current
    val window = (view.context as Activity).window

    WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = false


    LaunchedEffect(Unit) {
        viewModel.fetchItems()
    }

    when (state) {
        is ResultState.Loading -> {
            showProgressIndicator=true
//            Box(
//                modifier = Modifier
//                    .fillMaxSize() // Take full screen space
//            ) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center), // Center the indicator
//                    color = MaterialTheme.colorScheme.primary, // Use theme color or custom color
//                    strokeWidth = 4.dp // Optional: Adjust stroke width
//                )
//            }
        }

        is ResultState.Success -> {
            items = (state as ResultState.Success<List<RentalData>>).data
            showProgressIndicator=false

        }

        is ResultState.Error -> {
            val error = (state as ResultState.Error).message
            showProgressIndicator=false
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(color = colorResource(id = R.color.white))
    ) {
        if (items.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "No Property Yet!",
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.karla_medium)
                        ),
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // Define 2 columns in the grid
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                itemsIndexed(items) { index, item ->
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
        if(showProgressIndicator) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center), // Center the indicator
                    color = colorResource(id = R.color.color_926C57), // Use theme color or custom color
                    strokeWidth = 4.dp // Optional: Adjust stroke width
                )
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

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
                        Font(R.font.karla_regular)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 17.sp,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
                )
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = nameError,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black), // Set cursor color here
                focusedBorderColor = colorResource(id = R.color.black),
            )

        )
        if (nameError) {
            Text(
                text = stringResource(id = R.string.name_is_required),
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
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
                        Font(R.font.karla_regular)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 17.sp,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
                )
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = addressError,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black), // Set cursor color here
                focusedBorderColor = colorResource(id = R.color.black),
            )
        )
        if (addressError) {
            Text(
                text = stringResource(id = R.string.address_is_required),
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
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
                        Font(R.font.karla_regular)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 17.sp,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
                )
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            isError = rentAmountError,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black), // Set cursor color here
                focusedBorderColor = colorResource(id = R.color.black),
            )
        )
        if (rentAmountError) {
            Text(
                text = stringResource(id = R.string.rent_amount_is_required),
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
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
                        Font(R.font.karla_regular)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 17.sp,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
                )
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            isError = advanceAmountError,
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = colorResource(id = R.color.black),
                focusedBorderColor = colorResource(id = R.color.black),
            )
        )
        if (advanceAmountOverError) {
            Text(
                text = stringResource(id = R.string.advance_amount_cannot_exceed_rent_amount),
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
                )
            )
        }

        if (advanceAmountError) {
            Text(
                text = stringResource(id = R.string.advance_amount_is_required),
                color = MaterialTheme.colorScheme.error,
                fontFamily = FontFamily(
                    Font(R.font.karla_regular)
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
                    containerColor = colorResource(id = R.color.color_926C57), // Custom background color
                    contentColor = colorResource(id = R.color.white) // Custom text/icon color
                )
            ) {
                Text(
                    stringResource(id = R.string.save), modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.karla_semi_bold)
                        ),
                        fontSize = 20.sp,
                    )
                )
            }
        }
    }



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
                .padding(26.dp)
                .background(colorResource(id = R.color.white)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = colorResource(id = R.color.color_E6D7C3))

            ) {
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                ) {
                    Text(
                        text = "Add Tenant",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.karla_medium)
                            ),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp)
                            .clickable {
                                // Action for "Add Tenant"
                                onAddTenant()
                            },

                        )
                    Text(
                        text = "Allocate Tenant",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.karla_medium)
                            ),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        ),
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
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        MainScreen(viewModel(), viewModel(), viewModel())
    }
}