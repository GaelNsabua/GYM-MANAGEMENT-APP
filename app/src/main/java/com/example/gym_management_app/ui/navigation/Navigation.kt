package com.example.gym_management_app.ui.navigation


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gym_management_app.data.database.AppDatabase
import com.example.gym_management_app.data.repository.MemberRepository
import com.example.gym_management_app.data.repository.PaymentRepository
import com.example.gym_management_app.data.repository.SubscriptionRepository
import com.example.gym_management_app.ui.screens.AddMemberScreen
import com.example.gym_management_app.ui.screens.HomeScreen
import com.example.gym_management_app.ui.screens.LoginScreen
import com.example.gym_management_app.ui.screens.MemberListScreen
import com.example.gym_management_app.ui.screens.PaymentListScreen
import com.example.gym_management_app.ui.screens.SplashScreen
import com.example.gym_management_app.ui.screens.SubscriptionListScreen
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.MemberViewModelFactory
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModelFactory
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(){

    // Les ViewModels sont passés ici pour être accessibles dans chaque écran
    val context: Context = LocalContext.current
    val database: AppDatabase = remember { AppDatabase.getDatabase(context) }
    //Members
    val memberRepository: MemberRepository = remember { MemberRepository(database.memberDao()) }
    val memberViewModel: MemberViewModel = viewModel(factory = MemberViewModelFactory(memberRepository))
    //Subscription
    val subscriptionRepository : SubscriptionRepository = remember { SubscriptionRepository(database.subscriptionDao()) }
    val subscriptionViewModel: SubscriptionViewModel = viewModel(factory = SubscriptionViewModelFactory(subscriptionRepository))
    //Payment
    val paymentRepository : PaymentRepository = remember { PaymentRepository(database.paymentDao()) }
    val paymentViewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(paymentRepository, memberRepository, subscriptionRepository))

    // Crée ou récupère le NavController pour gérer la navigation entre les écrans
    val navController = rememberNavController()
    // Définition du NavHost, qui gère le système de navigation et définit l'écran de départ ("home")
    NavHost(navController = navController, startDestination = "splash") {
        // Écran de chargement (splash screen)
        composable("splash") {
            SplashScreen(navController = navController)
        }
        // Ecran de connexion (login screen)
        composable("login") {
            LoginScreen(navController = navController)
        }
        // Écran d'accueil (tableau de bord)
        composable("home") {
            HomeScreen(
                navController = navController,
                memberViewModel = memberViewModel,
                subscriptionViewModel = subscriptionViewModel,
                paymentViewModel = paymentViewModel
            )
        }
        composable("addMember") {
            AddMemberScreen(
                navController = navController,
                memberViewModel = memberViewModel,
                subscriptionViewModel = subscriptionViewModel
            )
        }
        // Écran de la liste des membres
        composable("memberList") {
            MemberListScreen(
                navController = navController,
                memberViewModel = memberViewModel
            )
        }
        // Écran de la liste des abonnements
        composable("subscriptionList") {
            SubscriptionListScreen(
                navController = navController,
                subscriptionViewModel = subscriptionViewModel
            )
        }
        // Écran de la liste des paiements
        composable("paymentList") {
            PaymentListScreen(
                navController = navController,
                paymentViewModel = paymentViewModel,
                memberViewModel = memberViewModel
            )
        }
        // Écran des rapports (à implémenter ou remplacer par votre composant)
        composable("reportScreen") {
            //ReportScreen(navController = navController)
        }
    }
}
