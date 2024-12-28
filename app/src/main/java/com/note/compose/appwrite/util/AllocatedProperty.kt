package com.note.compose.appwrite.util

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R
import com.note.compose.appwrite.model.RentalData
import com.note.compose.appwrite.model.TenantsData
import com.note.compose.appwrite.viewmodel.AllocatedViewModel
import com.note.compose.appwrite.viewmodel.MainViewModel
import com.note.compose.appwrite.viewmodel.ResultState
import com.note.compose.appwrite.viewmodel.TenantsViewModel
import java.util.Calendar

@Composable
fun AddEditAllocationScreen(
    onSave: () -> Unit,
    propertyViewModel: MainViewModel,
    tenantsViewModel: TenantsViewModel,
    allocatedViewModel: AllocatedViewModel,
) {

    val propertystate by propertyViewModel.state.collectAsState()
    val tenantsState by tenantsViewModel.state.collectAsState()
    var items by remember { mutableStateOf<List<RentalData>>(emptyList()) }
    var tenants by remember { mutableStateOf<List<TenantsData>>(emptyList()) }
    var propertys by remember { mutableStateOf<List<String>>(emptyList()) }
    var tenantsList by remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        propertyViewModel.fetchItems()
        tenantsViewModel.fetchItems()
    }

    when (propertystate) {
        is ResultState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }

        is ResultState.Success -> {
            items = (propertystate as ResultState.Success<List<RentalData>>).data
            propertys =
                items.map { it.name }
        }

        is ResultState.Error -> {
            val error = (propertystate as ResultState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }
    }
    when (tenantsState) {
        is ResultState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize() // Take full screen space
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
            }
        }

        is ResultState.Success -> {
            tenants = (tenantsState as ResultState.Success<List<TenantsData>>).data
            tenantsList = tenants.map { it.name }
        }

        is ResultState.Error -> {
            val error = (tenantsState as ResultState.Error).message
            Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        }

    }

    var selectedTenant by remember { mutableStateOf("") }
    var selectedProperty by remember { mutableStateOf("") }
    var rent by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var advance by remember { mutableStateOf("") }
    var isFreeAllocate by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    var tenantId by remember { mutableStateOf("") }
    var tenantName by remember { mutableStateOf("") }
    var tenantPhone by remember { mutableStateOf("") }
    var tenantAddress by remember { mutableStateOf("") }

    LaunchedEffect(selectedProperty) {
        val property = items.find { it.name == selectedProperty }

        property?.let {
            id = it.id
            rent = it.rentAmount.toString()
            advance = it.advanceAmount.toString()
            selectedTenant = if(it.allocatedTenantName.isNullOrEmpty()){""}else it.allocatedTenantName.toString()
            isFreeAllocate = it.isAllocated
            if (!it.startDate.isNullOrEmpty()) {
                startDate = it.startDate.toString()
            }
            if (!it.endDate.isNullOrEmpty()) {
                endDate = it.endDate.toString()
            }
        }
    }

    LaunchedEffect(selectedTenant) {
        val property = tenants.find { it.name == selectedTenant }
        property?.let {
            tenantId = it.id
            tenantName = it.name
            tenantPhone = it.phoneNumber
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enable vertical scrolling

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Selected Property Dropdown
        DropdownMenuField(
            label = "Selected Property",
            options = propertys,
            selectedOption = selectedProperty,
            onOptionSelected = { selectedProperty = it }
        )
        if (!isFreeAllocate) {
            DropdownMenuField(
                label = "Selected Tenant",
                options = tenantsList,
                selectedOption =selectedTenant,
                onOptionSelected = {selectedTenant = it }
            )
        } else {
            OutlinedTextField(
                value = if (!selectedTenant.isNullOrEmpty()) selectedTenant else "No Tenant Allocated",
                onValueChange = {},
                label = { Text(text = "Allocated Tenant", color = colorResource(id = R.color.color_979797),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 17.sp,
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = colorResource(id = R.color.color_838383),
                    disabledTextColor = colorResource(id = R.color.black),
                    disabledLabelColor = colorResource(id = R.color.color_838383)
                ),
                enabled = false
            )
        }

        OutlinedTextField(
            value = rent,
            onValueChange = { rent = it },
            label = {
                Text(
                    "Rent", color = colorResource(id = R.color.color_979797),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = advance,
            onValueChange = { advance = it },
            label = {
                Text(
                    "Advance", color = colorResource(id = R.color.color_979797),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (isFreeAllocate) "Allocate" else "Free",
                onValueChange = {},
                label = { Text(text = "Status", color = colorResource(id = R.color.color_979797),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )) },
                trailingIcon = {
                    Checkbox(
                        checked = isFreeAllocate,
                        onCheckedChange = { isFreeAllocate = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Black,
                            uncheckedColor = Color.Gray
                        )
                    )
                },
                readOnly = true, // Make the field read-only
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontSize = 17.sp,
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                ),
                // Customizing the border color directly
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Blue,
                    unfocusedLabelColor = Color.Gray
                )
            )
        }

        if (isFreeAllocate) {
            DatePickerWithInput(
                label = "Start Date",
                selectedDate = startDate,
                onDateSelected = { startDate = it },
            )
        }else {
            DatePickerWithInput(
                label = "End Date",
                selectedDate = endDate,
                onDateSelected = { endDate = it }
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

                    // Validation: Check if any required field is empty
                    if (selectedTenant.isEmpty() || selectedProperty.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please fill in all the required fields",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    // Validation: Ensure startDate and endDate are provided based on the allocation state
                    if (isFreeAllocate && startDate.isBlank()) {
                        Toast.makeText(
                            context,
                            "Start Date is required when allocating",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    if (!isFreeAllocate && endDate.isBlank()) {
                        Toast.makeText(
                            context,
                            "End Date is required when freeing",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    // Saving data
                    val selectedTenantsData = tenants.find { it.name == selectedTenant }
                    val selectedPropertyData = items.find { it.name == selectedProperty }
                    selectedPropertyData?.let { property ->
                        if (isFreeAllocate) {
                            endDate = ""
                        } else {
                            startDate = ""
                        }

                        val tenant = TenantsData(
                            id = tenantId,
                            name = tenantName,
                            address = tenantAddress,
                            phoneNumber = tenantPhone,
                            startDate = startDate,
                            endDate = endDate,
                            allocatedPropertyId = if (!isFreeAllocate) selectedPropertyData.id else null
                        )

                        allocatedViewModel.savePropertyAndTenant(
                            property = property.copy(
                                id = id,
                                rentAmount = rent.toInt(),
                                advanceAmount = advance.toInt(),
                                startDate = startDate,
                                endDate = endDate,
                                isAllocated = isFreeAllocate,
                                allocatedTenantId = if (!isFreeAllocate) tenantId else null,
                                allocatedTenantName = if(isFreeAllocate)tenantName else null
                            ),
                            tenant = tenant,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Data saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onSave() // Trigger any additional UI update or navigation
                                tenantId=""
                                tenantName=""
                                tenantAddress=""
                                startDate=""
                                endDate=""
                                id=""
                                rent=""
                                advance=""
                            },
                            onError = { error ->
                                Toast.makeText(
                                    context,
                                    "Error: $error",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Log.d("MyTesting", "Error: $error")
                            }
                        )
                    }
//                        onSave()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.color_07011c), // Custom background color
                    contentColor = colorResource(id = R.color.white) // Custom text/icon color
                ),

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

@Composable
fun DatePickerWithInput(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    // DatePickerDialog setup
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    if (showDatePickerDialog) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(
                    "%02d/%02d/%d",
                    selectedDay,
                    selectedMonth + 1,
                    selectedYear
                )
                onDateSelected(formattedDate)
                showDatePickerDialog = false
            },
            year,
            month,
            day
        ).show()
    }

    // Outlined TextField with Icon
    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        readOnly = true,
        label = {
            Text(
                text = label, color = colorResource(id = R.color.color_979797),
                fontFamily = FontFamily(
                    Font(R.font.crimsonpro_regular, FontWeight.Normal)
                )
            )
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.black),
            fontSize = 17.sp,
            fontFamily = FontFamily(
                Font(R.font.crimsonpro_regular, FontWeight.Normal)
            )
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Pick Date",
                modifier = Modifier.clickable { showDatePickerDialog = true }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePickerDialog = true }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedOption) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = {
                Text(
                    label,
                    color = colorResource(id = R.color.color_979797),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                )
            },
            textStyle = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 17.sp,
                fontFamily = FontFamily(
                    Font(R.font.crimsonpro_regular, FontWeight.Normal)
                )
            ),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        onOptionSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
