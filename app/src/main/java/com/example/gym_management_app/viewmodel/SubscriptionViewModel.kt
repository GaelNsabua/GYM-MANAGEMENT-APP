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

    init {
        loadSubscriptions()
    }

    // Charge tous les abonnements
    private fun loadSubscriptions() {
        viewModelScope.launch {
            repository.getAllSubscriptions().collect { subscriptionList ->
                _subscriptions.value = subscriptionList
            }
        }
    }

    // Ajoute un abonnement
    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            repository.insertSubscription(subscription)
        }
    }

    // Supprime un abonnement
    fun deleteSubscription(subscription: Subscription) {
        viewModelScope.launch {
            repository.deleteSubscription(subscription)
        }
    }

    // Vérifie si un abonnement est expiré
    fun isSubscriptionExpired(subscription: Subscription): Boolean {
        return System.currentTimeMillis() > subscription.endDate
    }
}
