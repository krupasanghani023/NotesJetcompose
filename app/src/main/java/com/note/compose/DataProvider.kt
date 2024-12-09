package com.note.compose

data class image(
    val id: Int,
    val ImageId: Int = 0
)
object DataProvider {
    val imageList = listOf(
        image(
            id = 1,
            ImageId = R.drawable.ic_2
        ),
        image(
            id = 2,
            ImageId = R.drawable.ic_3
        ),
        image(
            id = 3,
            ImageId = R.drawable.ic_5
        ),
        image(
            id = 4,
            ImageId = R.drawable.ic_2
        ),
        image(
            id = 5,
            ImageId = R.drawable.ic_4
        ),
        image(
            id = 6,
            ImageId = R.drawable.ic_3
        ),
        image(
            id = 7,
            ImageId = R.drawable.ic_5
        ),

    )
}