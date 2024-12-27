package com.note.compose.appwrite.util

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun RentalFormUI(
    onSave: () -> Unit,
    onCancel: () -> Unit,
    propertyViewModel: MainViewModel,
    tenantsViewModel: TenantsViewModel,
    allocatedViewModel: AllocatedViewModel,
) {

    val propertystate by propertyViewModel.state.collectAsState()
    val tenantsState by tenantsViewModel.state.collectAsState()
    var items by remember { mutableStateOf<List<RentalData>>(emptyList()) }
    var tenants by remember { mutableStateOf<List<TenantsData>>(emptyList()) }
    var notAllocatedProperty by remember { mutableStateOf<List<String>>(emptyList()) }
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
            items = (propertystate as ResultState.Success<List<RentalData>>).data
            notAllocatedProperty =
                items.map { it.name } // Extract property names
//            notAllocatedProperty =
//                items.filter { !it.isAllocated }.map { it.name } // Extract property names
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
                    modifier = Modifier.align(Alignment.Center), // Center the indicator
                    color = MaterialTheme.colorScheme.primary, // Use theme color or custom color
                    strokeWidth = 4.dp // Optional: Adjust stroke width
                )
            }
        }

        is ResultState.Success -> {
            tenants = (tenantsState as ResultState.Success<List<TenantsData>>).data
            tenantsList = tenants.map { it.name } // Extract property names
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

    // Update UI when selectedProperty changes
    LaunchedEffect(selectedProperty) {
        // Find the selected property by name
        val property = items.find { it.name == selectedProperty }

        // If property is found, update fields
        property?.let {
            id = it.id
            rent = it.rentAmount.toString()
            advance = it.advanceAmount.toString()
            isFreeAllocate = it.isAllocated
            if (!it.startDate.isNullOrEmpty()) {
                startDate = it.startDate.toString()
            }
            if (!it.endDate.isNullOrEmpty()) {
                startDate = it.endDate.toString()
            }
        }
    }

    LaunchedEffect(selectedTenant) {
        // Find the selected property by name
        val property = tenants.find { it.name == selectedTenant }

        // If property is found, update fields
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
        // Selected Tenant Dropdown
        DropdownMenuField(
            label = "Selected Tenant",
            options = tenantsList,
            selectedOption = selectedTenant,
            onOptionSelected = { selectedTenant = it }
        )

        // Selected Property Dropdown
        DropdownMenuField(
            label = "Selected Property",
            options = notAllocatedProperty,
            selectedOption = selectedProperty,
            onOptionSelected = { selectedProperty = it }
        )

        // Rent Input Field
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

        // Advance Input Field
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

//        // Free/Allocate Switch
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp, // Outline width
                    color = colorResource(id = R.color.color_838383), // Outline color changes based on state
                    shape = RoundedCornerShape(6.dp) // Optional: Rounded corners for outline
                )
                .padding(2.dp) // Padding to create space between outline and switch
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 13.dp, top = 2.dp, bottom = 2.dp)
            ) {

                Text(
                    text = "Free / Allocate",
                    color = if (isFreeAllocate) colorResource(id = R.color.black) else colorResource(
                        id = R.color.color_979797
                    ),
                    fontFamily = FontFamily(
                        Font(R.font.crimsonpro_regular, FontWeight.Normal)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = isFreeAllocate,
                    onCheckedChange = { isFreeAllocate = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black, // Thumb color when ON
                        checkedTrackColor = Color.Black, // Track color when ON
                        uncheckedThumbColor = Color.Gray, // Thumb color when OFF
                        uncheckedTrackColor = Color.LightGray // Track color when OFF
                    )
                )
            }
        }


        DatePickerWithInput(
            label = "Start Date",
            selectedDate = startDate,
            onDateSelected = { startDate = it },
        )
        DatePickerWithInput(
            label = "End Date",
            selectedDate = endDate,
            onDateSelected = { endDate = it }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Button(
                onClick = {
                    // Saving data
                    val selectedTenantsData = tenants.find { it.name == selectedTenant }
                    val selectedPropertyData = items.find { it.name == selectedProperty }
                    selectedPropertyData?.let { property ->
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
                                isAllocated = !isFreeAllocate,
                                allocatedTenantId = if (!isFreeAllocate) tenant.id else null
                            ),
                            tenant = tenant,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Data saved successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onSave() // Trigger any additional UI update or navigation
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
//                .menuAnchor()
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
