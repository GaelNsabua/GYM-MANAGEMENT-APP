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

    fun addPayment(payment: Payment) {
        viewModelScope.launch {
            repository.insertPayment(payment)
            loadMonthlyRevenue() // Recharger le revenu après ajout d'un paiement
        }
    }

    fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            repository.deletePayment(payment)
            loadMonthlyRevenue() // Recharger le revenu après suppression d'un paiement
        }
    }

    // La fonction addPaymentAndUpdateStatus reste inchangée
    fun addPaymentAndUpdateStatus(payment: Payment) {
        viewModelScope.launch {
            repository.insertPayment(payment)
            val member = memberRepository.getMemberById(payment.memberId)
            if (member != null) {
                val subscription = subscriptionRepository.getSubscriptionById(member.subscriptionId)
                if (subscription != null) {
                    val newEndDate = subscription.endDate + 30L * 24L * 60L * 60L * 1000L
                    val updatedSubscription = subscription.copy(endDate = newEndDate)
                    subscriptionRepository.updateSubscription(updatedSubscription)
                    val updatedMember = member.copy(isActive = System.currentTimeMillis() < newEndDate)
                    memberRepository.updateMember(updatedMember)
                }
            }
            loadMonthlyRevenue() // Mise à jour des revenus mensuels
        }
    }
}

