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

class PaymentViewModel(private val repository: PaymentRepository, private val memberRepository: MemberRepository,
                       private val subscriptionRepository: SubscriptionRepository
) : ViewModel() {

    // Liste des paiements
    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments

    init {
        loadPayments()
    }

    // Charge tous les paiements
    private fun loadPayments() {
        viewModelScope.launch {
            repository.getAllPayments().collect { paymentList ->
                _payments.value = paymentList
            }
        }
    }

    // Fonction pour obtenir la liste des paiements d'un membre spécifique
    fun getPaymentsForMember(memberId: Int): Flow<List<Payment>> {
        return repository.getPaymentsForMember(memberId)
    }

    // Ajoute un paiement
    fun addPayment(payment: Payment) {
        viewModelScope.launch {
            repository.insertPayment(payment)
        }
    }

    // Supprime un paiement
    fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            repository.deletePayment(payment)
        }
    }

    fun addPaymentAndUpdateStatus(payment: Payment) {
        viewModelScope.launch {
            // 1. Insérer le paiement
            repository.insertPayment(payment)

            // 2. Récupérer le membre associé au paiement
            val member = memberRepository.getMemberById(payment.memberId)
            if (member != null) {
                // 3. Récupérer l'abonnement correspondant au membre
                // Supposons que nous avons une méthode dans SubscriptionRepository pour récupérer par ID
                val subscription = subscriptionRepository.getSubscriptionById(member.subscriptionId)
                if (subscription != null) {
                    // 4. Calculer la nouvelle date de fin de l'abonnement
                    // Exemple : ajouter 30 jours (1 mois) au moment actuel si le paiement est pour un renouvellement mensuel
                    val newEndDate = subscription.endDate + 30L * 24L * 60L * 60L * 1000L

                    // Créer une copie mise à jour de l'abonnement avec la nouvelle date de fin
                    val updatedSubscription = subscription.copy(endDate = newEndDate)
                    subscriptionRepository.updateSubscription(updatedSubscription)

                    // 5. Mettre à jour le statut du membre selon la nouvelle date de fin
                    // Ici, on considère que le membre devient actif si la nouvelle date est dans le futur
                    val updatedMember = member.copy(isActive = System.currentTimeMillis() < newEndDate)
                    memberRepository.updateMember(updatedMember)
                }
            }
        }
    }
}
