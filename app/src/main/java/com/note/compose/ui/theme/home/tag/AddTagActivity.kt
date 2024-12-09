@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.note.compose.ui.theme.home.tag

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.note.compose.MyApplication
import com.note.compose.R
import com.note.compose.dataModels.Tag
import com.note.compose.ui.theme.home.tag.ui.theme.ComposeTheme
import com.note.compose.util.ResultState
import com.note.compose.viewModel.TagViewModel
import com.note.compose.viewModel.TagViewModelFactory
import javax.inject.Inject

class AddTagActivity : ComponentActivity() {
    @Inject
    lateinit var tagViewModelFactory: TagViewModelFactory
    private lateinit var tagViewModel: TagViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val windowInsetsController =
            WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        (applicationContext as MyApplication).appComponent.inject(this)

        setContent {
            tagViewModel =
                ViewModelProvider(this, tagViewModelFactory).get(TagViewModel::class.java)
            val tagJson = intent.getStringExtra("tag")
            val tag: Tag = Gson().fromJson(tagJson, Tag::class.java)
            ComposeTheme {
                    AddTag(tagViewModel,tag)
            }
        }
    }
}

@Composable
fun AddTag(viewModel: TagViewModel,tag:Tag) {
    var tagName by remember { mutableStateOf(tag.tagName) }
    val tagId by remember { mutableStateOf(tag.tagId) }
    val context = LocalContext.current // Get the context from the current Composable scope
    val tagState by viewModel.tagState

    when (tagState) {
        is ResultState.Loading -> {
            CircularProgressIndicator()
        }
        is ResultState.Success -> {
            (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
        }
        is ResultState.Error -> {
            val error = (tagState as ResultState.Error).message
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = colorResource(id = R.color.color_5E35B1),
                            titleContentColor = colorResource(id = R.color.white),
                        ),
                        title = {
                            Text(
                    text = if(tagId.isNullOrEmpty()){
                        stringResource(id = R.string.add_tag)}else{
                        stringResource(id = R.string.update_tag) },
                    fontSize = 25.sp,
                    color = colorResource(id = R.color.white),
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                )
                        },
                        navigationIcon = {

                IconButton(onClick = {
                    (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
                }) {
                    Icon(Icons.Default.ArrowBackIosNew, "", tint = colorResource(id = R.color.white))
                }
                        },

                        )
                },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // body
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                    ) {
                        // tagName Input Field
                        OutlinedTextField(
                            value = tagName,
                            onValueChange = { tagName = it },
                            label = { Text(stringResource(id = R.string.tag),fontFamily = FontFamily.Serif) },
                            placeholder = { Text(stringResource(id = R.string.enter_tag),fontFamily = FontFamily.Serif) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textStyle = TextStyle(fontFamily = FontFamily.Serif),
                            keyboardOptions = KeyboardOptions( imeAction = ImeAction.Done),
                        )

                        // save Button

                        OutlinedButton(
                            onClick = {
                                if(tagId.isNullOrEmpty()){
                                    viewModel.addTag(Tag(System.currentTimeMillis().toString(), tagName))
                                }
                                else{
                                    viewModel.editTag(tagId,tagName)
                                }
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 25.dp),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = colorResource(id = R.color.white),
                                containerColor = colorResource(id = R.color.color_5E35B1)
                            )
                        ) {
                            Text(
                                text = if(tagId.isNullOrEmpty()){stringResource(id = R.string.save)}else{
                                    stringResource(id = R.string.update)},
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp),fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }
            }
//            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_5E35B1))
//            Spacer(modifier = Modifier.height(2.dp)) // Adds space between the above text and the form
//
//            HorizontalDivider(thickness = 1.dp, color = colorResource(id = R.color.color_5E35B1))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTagPreview() {
    ComposeTheme {
        AddTag(viewModel(),Tag("",""))
    }
}