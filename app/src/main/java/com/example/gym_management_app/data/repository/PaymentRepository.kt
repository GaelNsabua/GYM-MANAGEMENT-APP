/**
 *Le repository sert d'interface entre les ViewModels et la base de données.
 * Il permet d'organiser proprement les accès aux données et d’assurer un bon découplage.
 */

package com.example.gym_management_app.data.repository

import com.example.gym_management_app.data.database.PaymentDao
import com.example.gym_management_app.data.models.Payment
import kotlinx.coroutines.flow.Flow

class PaymentRepository(private val paymentDao: PaymentDao) {

    // Récupère tous les paiements
    fun getAllPayments(): Flow<List<Payment>> = paymentDao.getAllPayments()

    // Ajoute un paiement
    suspend fun insertPayment(payment: Payment) = paymentDao.insert(payment)

    // Supprime un paiement
    suspend fun deletePayment(payment: Payment) = paymentDao.delete(payment)

    // Récupère la liste des paiements effectués par un membre
    fun getPaymentsForMember(memberId: Int): Flow<List<Payment>> {
        return paymentDao.getPaymentsForMember(memberId)
    }
}
