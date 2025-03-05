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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.data.repository.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    // Liste des abonnements
    private val _subscriptions = MutableStateFlow<List<Subscription>>(emptyList())
    val subscriptions: StateFlow<List<Subscription>> = _subscriptions
    // Statistique : nombre d'abonnements expirés
    private val _expiredSubscriptions = MutableStateFlow(0)
    val expiredSubscriptions: StateFlow<Int> = _expiredSubscriptions

    init {
        loadSubscriptions()
        loadExpiredSubscriptions()
    }

    // Charge tous les abonnements
    private fun loadSubscriptions() {
        viewModelScope.launch {
            repository.getAllSubscriptions().collect { subscriptionList ->
                _subscriptions.value = subscriptionList
            }
        }
    }

    private fun loadExpiredSubscriptions() {
        viewModelScope.launch {
            _expiredSubscriptions.value = repository.getExpiredSubscriptionsCount()
        }
    }

    // Ajoute un abonnement
    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            repository.insertSubscription(subscription)
            loadExpiredSubscriptions()
        }
    }

    // Supprime un abonnement
    fun deleteSubscription(subscription: Subscription) {
        viewModelScope.launch {
            repository.deleteSubscription(subscription)
            loadExpiredSubscriptions()
        }
    }

    // Vérifie si un abonnement est expiré
    fun isSubscriptionExpired(subscription: Subscription): Boolean {
        return System.currentTimeMillis() > subscription.endDate
    }

    // Fonction suspendue qui renvoie directement l'abonnement ou null
    suspend fun getSubscriptionForMember(id: Int): Subscription? {
        return repository.getSubscriptionById(id)
    }

    //Fonction qui renvoie l'abonnement d'un membre spécifique
    fun getSubscriptionById(id: Int): StateFlow<Subscription?> {
        val subscriptionFlow = MutableStateFlow<Subscription?>(null)

        viewModelScope.launch {
            repository.getSubscriptionById(id)?.let {
                subscriptionFlow.value = it
            }
        }
        return subscriptionFlow
    }

}
