@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.appwrite

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.note.compose.R
import com.note.compose.appwrite.model.RentalData
import com.note.compose.appwrite.ui.theme.ComposeTheme
import com.note.compose.appwrite.util.collectionId
import com.note.compose.appwrite.util.databaseId
import com.note.compose.appwrite.viewmodel.MainViewModel
import com.note.compose.appwrite.viewmodel.ResultState

class AppwriteDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        AppwriteClient.init(applicationContext)
        val database = AppwriteClient.database

        val viewModel = MainViewModel(
            database = database,
            databaseId = databaseId,
            collectionId = collectionId
        )

        setContent {
            ComposeTheme {
                MainScreen(viewModel=viewModel)
            }

        }
    }
}
@Composable
fun MainScreen(viewModel: MainViewModel) {
    var showAddEditScreen by remember { mutableStateOf(false) }


    if (showAddEditScreen) {
        AddEditScreen(
            viewModel = viewModel,
            onSave = { showAddEditScreen = false },
            onCancel = { showAddEditScreen = false }
        )
    } else {
        ItemListScreen(
            viewModel = viewModel,
            onAddItem = {
                viewModel.setCurrentItem(null)
                showAddEditScreen = true
            },
            onEditItem = {
                viewModel.setCurrentItem(it)
                showAddEditScreen = true
            }
        )
    }
}

@Composable
fun ItemListScreen(
    viewModel: MainViewModel,
    onAddItem: () -> Unit,
    onEditItem: (RentalData) -> Unit
) {
//    val items by viewModel.items.collectAsState()
    val state by viewModel.state.collectAsState()

    var items by remember { mutableStateOf<List<RentalData>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<String?>(null) }
    val view = LocalView.current
    val window = (view.context as Activity).window

    WindowCompat.getInsetsController(window,view)?.isAppearanceLightStatusBars = false


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
            }        }
        is ResultState.Success -> {
            items = (state as ResultState.Success<List<RentalData>>).data

        }
        is ResultState.Error -> {
            val error = (state as ResultState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItem,
                containerColor = colorResource(id = R.color.color_07011c),
                contentColor = colorResource(id = R.color.white)
            ) {
                Icon(Icons.Default.Add, "Add")
            }
        },

        topBar = {

            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.color_07011c),
                    titleContentColor = colorResource(id = R.color.white),
                ),

                title = {
                    Text(
                        "Property List",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                            ),
                            fontSize = 30.sp,
//                            color = colorResource(id = R.color.white)
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
}


@Composable
fun ConfirmDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RentItemRow(item: RentalData, onEdit: () -> Unit, onDelete: (String) -> Unit) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${item.name}",style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_medium, FontWeight.Normal)
                        ),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ))
//                    Text("Address: ${item.address}")
                    Text("${item.rentAmount}$",style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_regular, FontWeight.Normal)
                            ),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ))
//                    Text("Advance Amount: ${item.advanceAmount}")
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentWidth(Alignment.End), // Ensure only necessary space is used
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDelete(item.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }

}
@Composable
fun AddEditScreen(
    viewModel: MainViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    val currentItem by viewModel.currentItem.collectAsState()

    var name by remember { mutableStateOf(currentItem?.name ?: "") }
    var address by remember { mutableStateOf(currentItem?.address ?: "") }
    var rentAmount by remember { mutableStateOf(currentItem?.rentAmount?.toString() ?: "") }
    var advanceAmount by remember { mutableStateOf(currentItem?.advanceAmount?.toString() ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var rentAmountError by remember { mutableStateOf(false) }
    var advanceAmountError by remember { mutableStateOf(false) }
    Scaffold(

        topBar = {

                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colorResource(id = R.color.color_07011c),
                        titleContentColor = colorResource(id = R.color.white),
                    ),
                    title = { Text(if (currentItem == null) "Add Item" else "Edit Item",
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                            ),
                            fontSize = 30.sp,
//                            color = colorResource(id = R.color.white)
                        ),
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.fillMaxWidth()
                                                    )
                    },
                    navigationIcon  = {
                        IconButton(onClick =  onCancel ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = colorResource(id = R.color.white)
                            )
                        }
                    }
                )


        }
    )  { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column( modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val rainbowColors: List<Color> = listOf(colorResource(id = R.color.color_D81B60), colorResource(
                    id = R.color.color_5E35B1
                ), colorResource(id = R.color.color_00ACC1))
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
                    label = { Text("Name", color = colorResource(id = R.color.color_07011c)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(brush = brush, fontSize = 17.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = nameError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
//                        unfocusedBorderColor = Grey80,
                    )

                )
                if (nameError) {
                    Text(
                        text = "Name is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        addressError = false
                    },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(brush = brush, fontSize = 17.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = addressError
                )
                if (addressError) {
                    Text(
                        text = "Address is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = rentAmount,
                    onValueChange = {
                        rentAmount = it
                        rentAmountError = false
                    },
                    label = { Text("Rent Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(brush = brush, fontSize = 17.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    isError = rentAmountError
                )
                if (rentAmountError) {
                    Text(
                        text = "Rent amount is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = advanceAmount,
                    onValueChange = {
                        advanceAmount = it
                        advanceAmountError = false
                    },
                    label = { Text("Advance Amount") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(brush = brush, fontSize = 17.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    isError = advanceAmountError
                )
                if (advanceAmountError) {
                    Text(
                        text = "Advance amount is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Button(onClick = {
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
                                    rentAmount = rentAmount.toDoubleOrNull() ?: 0.0,
                                    advanceAmount = advanceAmount.toDoubleOrNull() ?: 0.0
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
                        Text("Save", modifier = Modifier.fillMaxWidth().padding(5.dp), textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                                ),
                                fontSize = 28.sp,
//                            color = colorResource(id = R.color.white)
                            ))
                    }
//                    Button(onClick = onCancel,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = colorResource(id = R.color.color_07011c), // Custom background color
//                            contentColor = colorResource(id = R.color.white) // Custom text/icon color
//                        )) {
//                        Text("Cancel")
//                    }
                }
            }

        }
    }
}



