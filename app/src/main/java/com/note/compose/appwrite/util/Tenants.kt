package com.note.compose.appwrite.util

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    var showProgressIndicator by remember { mutableStateOf(false) }

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
                    color = colorResource(id = R.color.color_926C57), // Use theme color or custom color
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
           showDeleteDialog=false
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
        if(showProgressIndicator) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center), // Center the indicator
                color = colorResource(id = R.color.color_926C57), // Use theme color or custom color
                strokeWidth = 4.dp // Optional: Adjust stroke width
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
    Box(modifier = Modifier
        .padding(vertical = 8.dp)
        .background(color = colorResource(id = R.color.white))
        .clickable { onEdit() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.white)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = colorResource(id = R.color.white))

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
                                    Font(R.font.karla_semi_bold)
                                ),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                       
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically, // Align the icon and text vertically centered
                                    horizontalArrangement = Arrangement.Start // Align them to the start (left side)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_phone_call), // Replace with your phone icon
                                        contentDescription = "Phone Icon",
                                        modifier = Modifier.size(15.dp), // Adjust the icon size
                                        tint = colorResource(id = R.color.color_00ACC1) // Set the icon color
                                    )

                                    Spacer(modifier = Modifier.width(4.dp)) // Add some space between the icon and the text

                                    Text(
                                        text = "${item.phoneNumber}",
                                        style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.karla_regular)),
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Start, // Align text to the left
                                            color = colorResource(id = R.color.color_00ACC1)
                                        ),
                                    )
                                }
                            }
                        
                    }
                    // Row for Edit and Delete icons with no extra space
                    Column(
                        modifier = Modifier
                            .align(Alignment.Top)
                            .padding(5.dp),
                    ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit_squre), // Custom icon from drawable
                                tint = colorResource(id = R.color.color_196B5A),
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .size(18.dp) // Fixed size for the icon
                                    .clickable { onEdit() }
                            )
                        Spacer(modifier = Modifier.height(8.dp)) // Add some space between the icon and the text
                         Icon(
                                painter = painterResource(id = R.drawable.ic_delete_round), // Custom icon from drawable
                                tint = colorResource(id = R.color.color_B50202),
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .size(18.dp) // Fixed size for the icon
                                    .clickable { onDelete(item.id) }
                            )
                    }
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
                        cursorColor = colorResource(id = R.color.color_926C57), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_926C57),
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
                        cursorColor = colorResource(id = R.color.color_926C57), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_926C57),
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
                    isError = phoneNumberError,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = colorResource(id = R.color.color_926C57), // Set cursor color here
                        focusedBorderColor = colorResource(id = R.color.color_926C57),
                    )
                )
                if (incorrectphoneNumberError) {
                    Text(
                        text = stringResource(id = R.string.invalid_phone_nuber),
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
                        .align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = {
                            // Save the item (add or edit)
                            // Validate inputs
                            nameError = name.isBlank()
                            addressError = address.isBlank()

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