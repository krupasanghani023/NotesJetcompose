@file:OptIn(ExperimentalGlideComposeApi::class)

package com.note.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.note.compose.ui.theme.ComposeTheme
import com.google.accompanist.pager.rememberPagerState

class ImageListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ComposeTheme {
                TopTabWithImageList()
            }

        }
    }
}
@Composable
fun TopTabWithImageList() {
    val tabs = listOf("Images", "Tab 2", "Tab 3")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        val imageContents = listOf(
            ImageContent(
                id = 1,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_2),
                    ImageData(id = 2, drawableResId = R.drawable.ic_3)
                )
            ),
            ImageContent(
                id = 2,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5)
                )
            ),
            ImageContent(
                id = 3,
                images = listOf(
                ImageData(id = 1, drawableResId = R.drawable.ic_4),
            )
        )
            ,
            ImageContent(
                id = 4,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5),
                    ImageData(id = 3, drawableResId = R.drawable.ic_2),
                    ImageData(id = 4, drawableResId = R.drawable.ic_3),
                    ImageData(id = 5, drawableResId = R.drawable.ic_4),
                    ImageData(id = 6, drawableResId = R.drawable.ic_2),
                )
            ),
            ImageContent(
                id = 5,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5)
                )
            ),
            ImageContent(
                id = 6,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4)
                )
            ),
            ImageContent(
                id = 7,
                images = listOf(
                    ImageData(id = 1, drawableResId = R.drawable.ic_4),
                    ImageData(id = 2, drawableResId = R.drawable.ic_5),
                    ImageData(id = 3, drawableResId = R.drawable.ic_2),
                )
            )
        )





        // Display image list based on the selected tab
        when (selectedTabIndex) {
            0 -> {
                ImageContentListWithPager(imageContents = imageContents)
            }
            1 -> { }
            2 -> { }
        }
    }
}
@Composable
fun ImageContentListWithPager(imageContents: List<ImageContent>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(imageContents) { imageContent ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                // HorizontalPager for images in each imageContent
                val pagerState = rememberPagerState()

                Box(modifier = Modifier.fillMaxWidth()) {
                    com.google.accompanist.pager.HorizontalPager(
                        state = pagerState,
                        count = imageContent.images.size, // The number of images in the list
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp) // Adjust the height as necessary
                    ) { page ->
                        val image = imageContent.images[page]
                        GlideImage(
                            model = image.drawableResId,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                            ,
                            contentScale = ContentScale.FillBounds
                        )
                    }

                    // HorizontalPager Indicator
                    if(pagerState.pageCount>1) {
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            activeColor = colorResource(id = R.color.color_5E35B1),
                            inactiveColor = colorResource(id = R.color.gray_400)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun TopTabWithImageListPreview() {
    ComposeTheme {
        TopTabWithImageList()
    }
}