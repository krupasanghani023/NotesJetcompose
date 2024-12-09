package com.note.compose.ui.theme.home.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.note.compose.R

@Composable
fun CenteredToolbar() {
    val text by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .shadow(elevation = 3.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { /*onSearchClicked()*/ },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            value = text,
            onValueChange = {
//                text = it
//                onTextChange(it.text)
            },
//            enabled = isEnabled,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize =16.sp,
                fontWeight = FontWeight.Bold
            ),
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
                    Text(
                        text = "hint",
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize =16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {/* onSearchClicked()*/ }),
            singleLine = true
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .size(40.dp)
                .background(color = Color.Transparent, shape = CircleShape)
                .clickable {
                },
        ) {
            if (text.text.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.ic_note_image),
                    contentDescription = stringResource(R.string.logout),
                    tint = MaterialTheme.colorScheme.primary,
                    )
            } else {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.ic_note_image),
                    contentDescription = stringResource(R.string.logout),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCenteredToolbar() {
    CenteredToolbar( )
}