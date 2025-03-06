package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gym_management_app.R
import com.example.gym_management_app.ui.components.BottomNavBar
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    memberViewModel: MemberViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Observer les statistiques depuis les ViewModels en temps réel
    val totalMembers by memberViewModel.totalMembers.collectAsState()
    val activeMembersCount by memberViewModel.activeMembers.collectAsState()
    val inactiveMembersCount by memberViewModel.inactiveMembers.collectAsState()
    val monthlyRevenue by paymentViewModel.monthlyRevenue.collectAsState()

    // Calculer la moyenne d'adhésion en divisant les membres actifs par le nombre des jours d'un mois (si > 0)
    val averageSubscription = if (totalMembers > 0)
        String.format("%.2f", (activeMembersCount.toDouble() / 30.00) * 100)
    else "0"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tableau de bord",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { BottomNavBar(navController, navController.currentDestination?.route) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Section des statistiques réparties sur plusieurs lignes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticCard(
                    title = "Total Membres",
                    value = totalMembers.toString(),
                    icon = R.drawable.ic_launcher_foreground,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatisticCard(
                    title = "Abonnements Actifs",
                    value = activeMembersCount.toString(),
                    icon = R.drawable.people,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticCard(
                    title = "Membres inactifs",
                    value = inactiveMembersCount.toString(),
                    icon = R.drawable.warning,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatisticCard(
                    title = "Revenus Mensuels",
                    value = "$monthlyRevenue $",
                    icon = R.drawable.ic_launcher_foreground,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            StatisticCard(

                title = "Moyenne Adhésion",
                value = "$averageSubscription %",
                icon = R.drawable.moneys,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(

                painter = painterResource(id = icon),

                contentDescription = title,

                tint = MaterialTheme.colorScheme.primary,

                modifier = Modifier.size(32.dp)

            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}