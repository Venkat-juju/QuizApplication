package com.example.quizapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.quizapplication.R

@Composable
fun NezyApp() {

    val navController = rememberNavController()
    val bottomBarVisibility = rememberSaveable { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisibility.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NezyBottomBar(
                    destinations = TopDestination.values().asList(),
                    selectedDestination = navController.currentBackStackEntryAsState().value?.destination,
                    onNavigateTo = { targetDestination ->
                        navController.navigate(
                            route = targetDestination.name,
                            navOptions = navOptions {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            NezyNavHost(
                navController = navController,
                bottomBarVisibility = bottomBarVisibility
            )
        }
    }
}

@Composable
fun NezyBottomBar(
    destinations: List<TopDestination>,
    selectedDestination: NavDestination?,
    onNavigateTo: (TopDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = selectedDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateTo(destination) },
                icon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(id = destination.selectedIconResId),
                            contentDescription = stringResource(id = destination.titleStringId),
                            modifier = Modifier.size(25.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = destination.unSelectedIconResId),
                            contentDescription = stringResource(id = destination.titleStringId),
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = destination.titleStringId)
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}

enum class TopDestination(
    val selectedIconResId: Int,
    val unSelectedIconResId: Int,
    val titleStringId: Int
) {
    TEST(
        selectedIconResId = R.drawable.ic_test_filled,
        unSelectedIconResId = R.drawable.ic_test_outlined,
        titleStringId = R.string.test
    ),
    LEARN(
        selectedIconResId = R.drawable.ic_book,
        unSelectedIconResId = R.drawable.ic_book_outline,
        titleStringId = R.string.learn
    ),
//    PROFILE(
//        selectedIconResId = R.drawable.ic_profile,
//        unSelectedIconResId = R.drawable.ic_profile_outlined,
//        titleStringId = R.string.profile
//    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false