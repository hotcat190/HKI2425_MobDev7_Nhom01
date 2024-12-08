package com.example.androidcookbook.ui.features.post.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.ui.features.newsfeed.PostHeader
import com.example.androidcookbook.data.mocks.SamplePosts

enum class DetailState {
    Description,
    Ingredient,
    Recipe
}

@Composable
fun PostDetailsScreen(
    post: Post,
    isLiked: Boolean,
    onLikedClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var state by remember { mutableStateOf(DetailState.Description) }
    val checkedStates: SnapshotStateList<Boolean> = remember {
        mutableStateListOf<Boolean>()
    }
    checkedStates.addAll(
        List(
            size = post.ingredient?.size ?: 0,
            init = {false}
        )
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 15.dp),
    ) {
        item {
            PostHeader(
                author = post.author,
                createdAt = post.createdAt
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.image_4),
//                    contentDescription = null,
//                    modifier =
//                    Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .clip(RoundedCornerShape(5)),
//                    contentScale = ContentScale.Crop,
//                )
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.mainImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(5)),
                    contentScale = ContentScale.Crop,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Like button
                OutlinedIconButton(
                    icon = (
                        if (isLiked) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        }
                    ),
                    onclick = onLikedClick
                )

                // Chat button
                IconButton(onClick = {}) {
                    if (isSystemInDarkTheme()) {

                        Image(
                            painter = painterResource(R.drawable.comment_icon_dark_theme),
                            modifier = Modifier.size(21.dp),

                            contentDescription = "Comment icon"
                        )

                    } else {

                        Image(
                            painter = painterResource(R.drawable.comment_icon_light_theme),
                            modifier = Modifier.size(21.dp),
                            contentDescription = "Comment icon"
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                // Share button
                OutlinedIconButton(icon = Icons.Outlined.Share) {

                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                LobsterTextButton(
                    onclick = { state = DetailState.Description },
                    text = "Description"
                )
                LobsterTextButton(onclick = { state = DetailState.Ingredient }, text = "Ingredient")
                LobsterTextButton(onclick = { state = DetailState.Recipe }, text = "Recipe")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 40.dp)
            ) {
                when (state) {
                    DetailState.Description -> {
                        Text(
                            text = post.description,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight(400),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        )
                    }

                    DetailState.Ingredient -> {
                        checkedStates.forEachIndexed { index, checked ->
                            val ingredientText: String
                                = post.ingredient?.get(index)?.name + " " + post.ingredient?.get(index)?.quantity
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val bulletColor = MaterialTheme.colorScheme.secondary
                                Canvas(modifier = Modifier.size(12.dp)) {
                                    drawCircle(bulletColor)
                                }
                                Text(
                                    text = ingredientText,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = { isChecked ->
                                        checkedStates[index] = isChecked
                                    },
                                    colors = CheckboxDefaults.colors(

                                        checkmarkColor = MaterialTheme.colorScheme.secondary,
                                        uncheckedColor = MaterialTheme.colorScheme.secondary,
                                        checkedColor = Color(101, 85, 143)
                                    )
                                )
                            }
                        }
                    }

                    DetailState.Recipe -> {
                        post.steps?.forEachIndexed {index, stepText ->
                            Text(
                                text = "${index+1}. $stepText\n",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight(400),
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onclick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onclick
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun LobsterTextButton(
    modifier: Modifier = Modifier,
    onclick: () -> Unit,
    text: String
) {
    Button(
        modifier = modifier
            .wrapContentHeight()
            .width(110.dp)
            .padding(5.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(size = 999.dp),
        onClick = onclick
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.lobster_regular)),
                fontWeight = FontWeight(400),
                color = MaterialTheme.colorScheme.secondary,
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailsPreview() {
    PostDetailsScreen(SamplePosts.posts[0], false, {})
}