package com.recipesbook.navigations

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.recipesbook.ui.theme.Dimensions

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

    NavigationBar(modifier = Modifier.height(Dimensions.NavBar.height)) {
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
                    tint = if (i == selectedTabIndex) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                )}
            )
        }
    }
}