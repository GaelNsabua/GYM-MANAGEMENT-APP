package com.example.gym_management_app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym_management_app.R

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String?) {
     NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surfaceTint) },
                label = { Text("Accueil") },
                selected = navController.currentDestination?.route == "home",
                onClick = { navController.navigate("home") }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.people), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surfaceTint) },
                label = { Text("Membres") },
                selected = navController.currentDestination?.route == "memberList",
                onClick = { navController.navigate("memberList") }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.list), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surfaceTint)},
                label = { Text("Abonnements") },
                selected = navController.currentDestination?.route == "subscriptionList",
                onClick = { navController.navigate("subscriptionList") }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.money), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surfaceTint)},
                label = { Text("Paiements") },
                selected = navController.currentDestination?.route == "paymentList",
                onClick = { navController.navigate("paymentList") }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = R.drawable.reports), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.surfaceTint)},
                label = { Text("Rapports") },
                selected = navController.currentDestination?.route == "reportScreen",
                onClick = { navController.navigate("reportScreen") }
            )
        }
    }