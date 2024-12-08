package com.example.androidcookbook.ui.common.appbars

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookbookAppBarDefault(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    onCreatePostClick: () -> Unit = {},
    searchButtonAction: () -> Unit = {},
    onMenuButtonClick: () -> Unit = {},
    onBackButtonClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    onLogoutClick: () -> Unit = {}
) {

    var menuExpanded by remember { mutableStateOf(false) } // State to control menu visibility


    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(
                text = "Cookbook",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        actions = {
            Box(modifier = Modifier.padding(end = 6.dp)) {
                IconButton(
                    onClick = onCreatePostClick,
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
//                    Image(
//                        modifier = Modifier.size(20.dp),
//                        painter = painterResource(R.drawable.plus),
//                        contentDescription = "Create Post",
//                        contentScale = ContentScale.Crop
//
//                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Post",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Box(modifier = Modifier.padding(end = 6.dp)) {
                IconButton(
                    onClick = searchButtonAction,
                    modifier = Modifier.size(36.dp),
                ) {
//                    Image(
//                        modifier = Modifier.size(24.dp),
//                        painter = painterResource(R.drawable.search_interface_symbol),
//                        contentDescription = "Search",
//                        contentScale = ContentScale.Crop
//                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Box(modifier = Modifier.padding(end = 6.dp)) {
                IconButton(
//                    onClick = onMenuButtonClick,
                    onClick = {menuExpanded = !menuExpanded},
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu Button",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }, // Close the menu when clicked outside
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface) // Set background to white
                            .clip(RoundedCornerShape(12.dp)) // Consistent corner rounding
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false

                            },
                            text = {
                                Text(
                                    text = "Settings",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface // Black text for contrast
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 0.dp) // No extra padding to align with the main menu
                                .height(48.dp)
                                .fillMaxWidth()
                        )
                        DropdownMenuItem(
                            onClick = {
                                menuExpanded = false
                                onLogoutClick()
                            },
                            text = {
                                Text(
                                    text = "Logout",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface // Black text for contrast
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = null,

                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 0.dp) // No extra padding to align with the main menu
                                .height(48.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back Button",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    AppBarTheme {
        CookbookAppBarDefault()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopAppBarDarkPreview() {
    AppBarTheme(darkTheme = true) {
        CookbookAppBarDefault()
    }
}