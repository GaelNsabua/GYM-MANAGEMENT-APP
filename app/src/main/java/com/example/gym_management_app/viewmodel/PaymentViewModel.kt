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
import com.example.gym_management_app.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {

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
}
