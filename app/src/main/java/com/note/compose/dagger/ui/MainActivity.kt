package com.note.compose.dagger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.note.compose.TopTabWithImageList
import com.note.compose.dagger.MyApplication
import com.note.compose.dagger.di.ViewModelFactory
import com.note.compose.dagger.ui.composables.TopTabWithImageListNew
import com.note.compose.dagger.ui.ui.theme.ComposeTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TopTabViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).applicationComponent.inject(this)

        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        viewModel = ViewModelProvider(this, viewModelFactory)[TopTabViewModel::class.java]

        setContent {
            ComposeTheme {
                MaterialTheme {
                    TopTabWithImageListNew(viewModel)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTheme {
        TopTabWithImageListNew(viewModel())
    }
}