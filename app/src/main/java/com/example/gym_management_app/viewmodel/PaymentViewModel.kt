/**
 *Les ViewModels permettent de séparer la logique métier de l’interface utilisateur
 * et d'assurer la persistance des données en cas de rotation de l’écran
 * ou d'autres changements de configuration
 *
 * Ils utilisent les DAO via les repositories pour exposer
 * les données à l’interface utilisateur.
 */

package com.example.gym_management_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gym_management_app.data.models.Payment
import com.example.gym_management_app.data.repository.MemberRepository
import com.example.gym_management_app.data.repository.PaymentRepository
import com.example.gym_management_app.data.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class PaymentViewModel(
    private val repository: PaymentRepository,
    private val memberRepository: MemberRepository,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    // Liste des paiements
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments

    // Statistique : revenu mensuel
    private val _monthlyRevenue = MutableStateFlow(0.0)
    val monthlyRevenue: StateFlow<Double> = _monthlyRevenue

    init {
        loadPayments()
        loadMonthlyRevenue()
    }

    private fun loadPayments() {
        viewModelScope.launch {
            repository.getAllPayments().collect { paymentList ->
                _payments.value = paymentList
            }
        }
    }

    private fun loadMonthlyRevenue() {
        viewModelScope.launch {
            // Exemple : calculer le revenu depuis le début du mois jusqu'à maintenant
            val now = System.currentTimeMillis()
            // Calculer le début du mois (pour une solution plus précise, utilisez java.time.YearMonth)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfMonth = calendar.timeInMillis
            _monthlyRevenue.value = repository.getMonthlyRevenue(startOfMonth, now)
        }
    }

    fun getPaymentsForMember(memberId: Int): Flow<List<Payment>> {
        return repository.getPaymentsForMember(memberId)
    }

    fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            repository.deletePayment(payment)
            loadMonthlyRevenue() // Recharger le revenu après suppression d'un paiement
        }
    }

    fun addPaymentAndUpdateStatus(payment: Payment) {
        viewModelScope.launch {
            // Insère le paiement dans la base
            repository.insertPayment(payment)

            // Récupère le membre associé au paiement
            val member = memberRepository.getMemberById(payment.memberId)
            if (member != null) {
                // Calculer la nouvelle date de fin de l'abonnement en ajoutant 30 jours à l'ancienne date de fin
                val newEndDate = member.endDate + 30L * 24L * 60L * 60L * 1000L

                // Détermine si le membre doit être actif : actif si la date actuelle est inférieure à la nouvelle date
                val isActiveUpdated = System.currentTimeMillis() < newEndDate

                // Créer une copie mise à jour du membre avec la nouvelle date et le nouveau statut
                val updatedMember = member.copy(endDate = newEndDate, isActive = isActiveUpdated)

                // Met à jour le membre dans la base de données
                memberRepository.updateMember(updatedMember)
            }
            // Met à jour le revenu mensuel après l'ajout du paiement
            loadMonthlyRevenue()
        }
    }

}

