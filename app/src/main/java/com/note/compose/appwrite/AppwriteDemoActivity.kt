@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.compose.appwrite

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.note.compose.appwrite.util.Property_Id
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
            collectionId = Property_Id
        )

        setContent {
            ComposeTheme {
//                MainScreen(viewModel=viewModel)
            }

        }
    }
}
//@Composable
//fun MainScreen(viewModel: MainViewModel) {
//    var showAddEditScreen by remember { mutableStateOf(false) }
//
//
//    if (showAddEditScreen) {
//        AddEditScreen(
//            viewModel = viewModel,
//            onSave = { showAddEditScreen = false },
//            onCancel = { showAddEditScreen = false }
//        )
//    } else {
//        ItemListScreen(
//            viewModel = viewModel,
//            onAddItem = {
//                viewModel.setCurrentItem(null)
//                showAddEditScreen = true
//            },
//            onEditItem = {
//                viewModel.setCurrentItem(it)
//                showAddEditScreen = true
//            }
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: MainViewModel,
    onAddItem: () -> Unit,
    onEditItem: (RentalData) -> Unit
) {
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // Background overlay
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dialog Title
                Text(
                    text = "Confirm Deletion",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Dialog Message
                Text(
                    text = "Are you sure you want to delete this item?",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Right
                ) {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text("Cancel")
                    }

                    TextButton(
                        onClick = onConfirm,
                    ) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
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
                    .padding(top = 8.dp, start = 16.dp, bottom = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("${item.name}",style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_medium, FontWeight.Normal)
                        ),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    ))
                    Spacer(modifier = Modifier.height(5.dp))
                    Card(
                        modifier = Modifier,
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .padding(horizontal = 2.dp)
                        ) {
                            Text(
                                "$${item.rentAmount}",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.crimsonpro_regular, FontWeight.Normal)),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                ),
                            )
                        }
                    }
                }
                // Row for Edit and Delete icons with no extra space
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit), // Custom icon from drawable
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp) // Fixed size for the icon
                        )
                    }

                    IconButton(onClick = { onDelete(item.id) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete), // Custom icon from drawable
                            contentDescription = "Delete",
                            modifier = Modifier.size(24.dp) // Fixed size for the icon
                        )
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
    var advanceAmountOverError by remember { mutableStateOf(false) }
    Scaffold(

        topBar = {

                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = colorResource(id = R.color.color_07011c),
                        titleContentColor = colorResource(id = R.color.white),
                    ),
                    title = { Text(
                        if (currentItem == null) stringResource(id = R.string.add_item) else stringResource(
                            id = R.string.edit_item
                        ),
                        style = TextStyle(
                            fontFamily = FontFamily(
                                Font(R.font.crimsonpro_semibold, FontWeight.Normal)
                            ),
                            fontSize = 30.sp,
                        ),
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
                    label = { Text(stringResource(id = R.string.name), color = colorResource(id = R.color.color_979797),fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = colorResource(id = R.color.black), fontSize = 17.sp,fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )),
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
                    label = { Text(stringResource(id = R.string.address), color = colorResource(id = R.color.color_979797),fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = colorResource(id = R.color.black), fontSize = 17.sp,fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )),
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
                    label = { Text(stringResource(id = R.string.rent_amount), color = colorResource(id = R.color.color_979797),fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = colorResource(id = R.color.black), fontSize = 17.sp,fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
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
                    label = { Text(stringResource(id = R.string.advance_amount), color = colorResource(id = R.color.color_979797), fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = colorResource(id = R.color.black), fontSize = 17.sp, fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
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
                            ))
                    }
                }
            }

        }
    }
}



