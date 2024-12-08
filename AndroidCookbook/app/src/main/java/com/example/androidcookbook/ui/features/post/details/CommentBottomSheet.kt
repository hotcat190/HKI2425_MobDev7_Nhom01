package com.example.androidcookbook.ui.features.post.details

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.androidcookbook.domain.model.post.Comment
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.common.iconbuttons.LikeButton
import com.example.androidcookbook.ui.features.newsfeed.SmallAvatar
import com.example.androidcookbook.ui.theme.transparentTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    comments: List<Comment>,
    user: User,
    onSendComment: (String) -> Unit,
    onDeleteComment: (Comment) -> Unit,
    onEditComment: (Comment) -> Unit,
    onLikeComment: (Comment) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
            .safeDrawingPadding()
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .weight(1F),
        ) {
            Text(
                text = "Comments",
                fontWeight = FontWeight.Bold,
            )
            LazyColumn {
                items(comments) { comment ->
                    CommentRow(
                        comment = comment,
                        user = user,
                        onDeleteComment = onDeleteComment,
                        onEditComment = onEditComment,
                        onLikeComment = onLikeComment,
                    )
                }
            }
        }
        HorizontalDivider()

        WriteCommentRow(user, onSendComment)
    }
}

@Composable
private fun CommentRow(
    comment: Comment,
    user: User,
    onDeleteComment: (Comment) -> Unit,
    onEditComment: (Comment) -> Unit,
    onLikeComment: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isContextMenuVisible by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var itemHeight by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .indication(interactionSource, LocalIndication.current)
            .onSizeChanged {
                itemHeight = with(localDensity) { it.height.toDp() }
            }

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
        ) {
            SmallAvatar(
                author = comment.user,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1F)
            ) {
                Row(
                    modifier = Modifier
                ) {
                    Text(
                        text = comment.user.name,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = comment.createdAt,
                        fontSize = TextUnit(
                            value = 12f,
                            type = TextUnitType.Sp,
                        ),
                    )
                }
                // Comment Content
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = comment.content, modifier = Modifier.fillMaxSize()
                    )
                }
            }
//            Spacer(Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                LikeButton(
                    isLiked = comment.isLiked,
                    onLikedClick = {
                        onLikeComment(comment)
                    },
                )
                Text(
                    text = comment.likes.toString(),
                )
            }
        }

        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            offset = pressOffset.copy(
//                            x = pressOffset.x.coerceAtMost(240.dp),
                y = pressOffset.y - itemHeight
            ),
        ) {
            if (user.id == comment.user.id) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        isContextMenuVisible = false
                        onEditComment(comment)
                    },
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        isContextMenuVisible = false
                        onDeleteComment(comment)
                    },
                )
            }
//            else {
//                DropdownMenuItem(
//                    text = { Text("Hide") },
//                    onClick = {
//                        isContextMenuVisible = false
//                    },
//                )
//            }
        }
    }
}

@Composable
private fun WriteCommentRow(
    user: User,
    onSendComment: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        var commentContent by remember { mutableStateOf("") }

        SmallAvatar(user)
        Spacer(Modifier.width(8.dp))
        TextField(
            value = commentContent,
            onValueChange = { commentContent = it },
            placeholder = {
                Text("Write a comment...")
            },
            colors = transparentTextFieldColors(),
            modifier = Modifier.weight(1F)
        )
        IconButton(
            onClick = {
                onSendComment(commentContent)
                commentContent = ""
                focusManager.clearFocus()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CommentBottomSheetPreview() {
    CommentBottomSheetTheme {
        CommentBottomSheet(comments = listOf(
            Comment(
                content = "Lorem ipsum dolor sit amet, small consectetur adipiscing elit.",
            ),
            Comment(
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            ),
            Comment(
                content = "Lorem dolor sit amet, small consectetur elit.",
            ),
            Comment(
                content = "Lorem ipsum dolor sit, small consectetur adipiscing elit.",
            ),
            Comment(
                content = "Lorem sit amet, small consectetur adipiscing elit.",
            ),
            Comment(
                content = "Lorem sit amet.",
            ),
            Comment(
                content = "Lorem sit amet, small consectetur adipiscing elit, lorem sit amet, small consectetur adipiscing elit, small,",
            ),
        ), user = User(
            id = 1,
            name = "Username",
        ), onSendComment = {},{},{},{}, onDismiss = {}, sheetState = SheetState(
            skipPartiallyExpanded = true,
            density = Density(LocalContext.current),
            initialValue = SheetValue.Expanded,
            confirmValueChange = { true },
        ), modifier = Modifier
        )
    }
}