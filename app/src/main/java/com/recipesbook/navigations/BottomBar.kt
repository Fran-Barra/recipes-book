package com.recipesbook.navigations

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.core.graphics.toColor
import com.recipesbook.R

@Composable
fun BottomBar(onNavigate: (String)->Unit) {
    val homeTab = TabBarItem(RecipesBookScreen.Home, Icons.Outlined.Home)
    val favouritesTab = TabBarItem(RecipesBookScreen.Favourites, Icons.Outlined.FavoriteBorder)
    val vaultTab = TabBarItem(RecipesBookScreen.Vault, Icons.Outlined.Lock)

    val tabBarItems = listOf(homeTab, favouritesTab, vaultTab)

    TabView(tabBarItems, onNavigate)
}

data class TabBarItem(val screen: RecipesBookScreen, val icon: ImageVector)

@Composable
fun TabView(
    tabBarItems: List<TabBarItem>,
    onNavigate: (String) -> Unit
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    //TODO: move the icon color to a config file
    NavigationBar {
        tabBarItems.forEachIndexed { i, item ->
            NavigationBarItem(
                selected = i == selectedTabIndex,
                onClick = {
                    selectedTabIndex = i
                    onNavigate(item.screen.name)
                },
                icon = { Icon(
                    imageVector = item.icon,
                    contentDescription = item.screen.name,
                    tint = if (i == selectedTabIndex) colorResource(id = R.color.black)
                            else colorResource(id = R.color.gray)
                )}
            )
        }
    }
}