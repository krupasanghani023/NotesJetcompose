package com.note.compose.appwrite.util

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.note.compose.R
import com.note.compose.appwrite.ConfirmDeleteDialog
import com.note.compose.appwrite.model.TenantsData
import com.note.compose.appwrite.viewmodel.ResultState
import com.note.compose.appwrite.viewmodel.TenantsViewModel

@Composable
fun TenantsListScreen(
    viewModel: TenantsViewModel,
    onEditItem: (TenantsData) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    var items by remember { mutableStateOf<List<TenantsData>>(emptyList()) }
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
            items = (state as ResultState.Success<List<TenantsData>>).data

        }

        is ResultState.Error -> {
            val error = (state as ResultState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
            Log.d("MyTesting", "error:--$error")
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
                    "No Tenants Yet!",
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
                    TenantsItemRow(
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
fun TenantListScreen(navController: NavController, tenantsViewModel: TenantsViewModel) {
    TenantsListScreen(
        viewModel = tenantsViewModel,
        onEditItem = {
            tenantsViewModel.setCurrentItem(it)
            navController.navigate("add_edit_tenant")

        }
    )
}

@Composable
fun TenantsItemRow(item: TenantsData, onEdit: () -> Unit, onDelete: (String) -> Unit) {

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
                Text(
                    "${item.name}", style = TextStyle(
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_medium, FontWeight.Normal)
                        ),
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                )
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
                            "${item.phoneNumber}",
                            style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.crimsonpro_regular,
                                        FontWeight.Normal
                                    )
                                ),
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
fun AddEditTenantScreen(onSave: () -> Unit, tenantsViewModel: TenantsViewModel) {

    val currentItem by tenantsViewModel.currentItem.collectAsState()

    var name by remember { mutableStateOf(currentItem?.name ?: "") }
    var address by remember { mutableStateOf(currentItem?.address ?: "") }
    var phoneNumber by remember { mutableStateOf(currentItem?.phoneNumber?.toString() ?: "") }
    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var phoneNumberError by remember { mutableStateOf(false) }
    var incorrectphoneNumberError by remember { mutableStateOf(false) }
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
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        phoneNumberError = false
                    },
                    label = {
                        Text(
                            stringResource(id = R.string.contact),
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
                    isError = phoneNumberError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_07011c), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_07011c),
                    )
                )
                if (incorrectphoneNumberError) {
                    Text(
                        text = stringResource(id = R.string.invalid_phone_nuber),
                        color = MaterialTheme.colorScheme.error,
                        fontFamily = FontFamily(
                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
                        )
                    )
                }
//                if (phoneNumberError) {
//                    Text(
//                        text = stringResource(id = R.string.phone_number_is_required),
//                        color = MaterialTheme.colorScheme.error,
//                        fontFamily = FontFamily(
//                            Font(R.font.crimsonpro_regular, FontWeight.Normal)
//                        )
//                    )
//                }


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
//                            phoneNumberError = phoneNumber.isBlank()

                            phoneNumberError = phoneNumber.isBlank()
                            incorrectphoneNumberError=!phoneNumber.matches(Regex("^[0-9]{10}$"))


                            // If no errors, save the data
                            if (!nameError && !addressError && !phoneNumberError && !incorrectphoneNumberError) {

                                tenantsViewModel.saveItem(
                                    TenantsData(
                                        id = currentItem?.id ?: "",
                                        name = name,
                                        address = address,
                                        phoneNumber = phoneNumber,
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