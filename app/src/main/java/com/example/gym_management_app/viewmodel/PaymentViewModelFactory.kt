package com.example.gym_management_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gym_management_app.data.repository.MemberRepository
import com.example.gym_management_app.data.repository.PaymentRepository
import com.example.gym_management_app.data.repository.SubscriptionRepository

class PaymentViewModelFactory(
    private val paymentRepository: PaymentRepository,
    private val memberRepository: MemberRepository,
    private val subscriptionRepository: SubscriptionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Vérifie que le modelClass est bien PaymentViewModel
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Passe les trois dépendances au constructeur du PaymentViewModel
            return PaymentViewModel(paymentRepository, memberRepository, subscriptionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
