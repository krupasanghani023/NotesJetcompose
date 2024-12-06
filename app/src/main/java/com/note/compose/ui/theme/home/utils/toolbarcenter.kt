package com.note.compose.ui.theme.home.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.note.compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredToolbar(
    title: String,
    onBackPressed: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val (titleRef, tagRef) = createRefs()

        // Title Text
        Text(
            text = "alk;aswoikodre  iojkm fillToConstraints   ",
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            fontFamily = FontFamily.Serif,
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end) // Add space between title and tag
                width = Dimension.fillToConstraints  // Title respects constraints
            }
        )

        // Tag Text
        Text(
            text =" note.noteTag",
            fontSize = 13.sp,
            fontFamily = FontFamily.Serif,
            color = Color.Gray,
            modifier = Modifier.constrainAs(tagRef) {
                start.linkTo(tagRef.end)
                bottom.linkTo(titleRef.bottom) // Align tag to the bottom of the title
                end.linkTo(tagRef.end) // Align tag to the end of the parent
                width = Dimension.wrapContent // Title respects constraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCenteredToolbar() {
    CenteredToolbar(
        title = "Centered Title",
        onBackPressed = { /* Handle back press */ }
    )
}