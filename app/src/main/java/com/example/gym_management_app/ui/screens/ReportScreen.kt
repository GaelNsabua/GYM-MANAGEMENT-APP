package com.example.gym_management_app.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.graphics.Paint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavHostController,
    memberViewModel: MemberViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var reportGenerated by remember { mutableStateOf(false) }
    var reportPath by remember { mutableStateOf("") }

    // Interface utilisateur pour générer le rapport PDF
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rapports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Génération de rapports",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // Génère le rapport PDF et met à jour l'état
                    reportPath = generatePdfReport(context, memberViewModel, subscriptionViewModel, paymentViewModel)
                    reportGenerated = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Générer le rapport PDF")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (reportGenerated) {
                Text(
                    text = "Rapport généré à : $reportPath",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { openPdfReport(context, reportPath) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ouvrir le rapport")
                }
            }
        }
    }
}

/**
 * Génère un rapport PDF en utilisant les données des ViewModels.
 * Pour simplifier, ce rapport affiche un titre et quelques statistiques de base.
 * Retourne le chemin absolu du fichier PDF généré.
 */
fun generatePdfReport(
    context: Context,
    memberViewModel: MemberViewModel,
    subscriptionViewModel: SubscriptionViewModel,
    paymentViewModel: PaymentViewModel
): String {
    // Création d'une instance PdfDocument
    val pdfDocument = PdfDocument()

    // Configuration de la page (largeur, hauteur, numéro de page)
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    // Titre du rapport
    paint.textSize = 14f
    canvas.drawText("Rapport de la salle de sport", 10f, 25f, paint)

    // Pour un rapport complet, vous pouvez récupérer les statistiques depuis les ViewModels
    // Ici, nous utilisons des valeurs fictives pour l'exemple
    val totalMembers =  memberViewModel.totalMembers.value
    val activeMembers = memberViewModel.activeMembers.value
    val monthlyRevenue = paymentViewModel.monthlyRevenue.value
    val inactiveMember = memberViewModel.inactiveMembers.value

    paint.textSize = 12f
    canvas.drawText("Total Membres : $totalMembers", 10f, 50f, paint)
    canvas.drawText("Abonnements Actifs : $activeMembers", 10f, 70f, paint)
    canvas.drawText("Membres inactifs : $inactiveMember", 10f, 90f, paint)
    canvas.drawText("Revenus Mensuels : $monthlyRevenue Fc", 10f, 110f, paint)

    pdfDocument.finishPage(page)

    // Sauvegarder le PDF dans le cache externe
    val file = File(context.externalCacheDir, "report${generateRandomCode()}.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    pdfDocument.close()
    return file.absolutePath
}

/**
 * Ouvre le rapport PDF en utilisant un FileProvider.
 * Assurez-vous que FileProvider est correctement configuré dans AndroidManifest et res/xml/file_paths.xml.
 */
fun openPdfReport(context: Context, filePath: String) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(intent)
}
