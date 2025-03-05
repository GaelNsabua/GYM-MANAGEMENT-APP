/**
 *Le repository sert d'interface entre les ViewModels et la base de données.
 * Il permet d'organiser proprement les accès aux données et d’assurer un bon découplage.
 */

package com.example.gym_management_app.data.repository

import com.example.gym_management_app.data.database.SubscriptionDao
import com.example.gym_management_app.data.models.Subscription
import kotlinx.coroutines.flow.Flow

class SubscriptionRepository(private val subscriptionDao: SubscriptionDao) {

    // Récupère tous les abonnements
    fun getAllSubscriptions(): Flow<List<Subscription>> = subscriptionDao.getAllSubscriptions()

    // Ajoute un abonnement
    suspend fun insertSubscription(subscription: Subscription) = subscriptionDao.insert(subscription)

    // Supprime un abonnement
    suspend fun deleteSubscription(subscription: Subscription) = subscriptionDao.delete(subscription)

    // Récupère un abonnement spécifique par son ID
    suspend fun getSubscriptionById(id: Int): Subscription? {
        return subscriptionDao.getSubscriptionById(id)
    }

    suspend fun getExpiredSubscriptionsCount(): Int =
        subscriptionDao.getExpiredSubscriptionsCount()

    //Met à jour la date de fin d'un abonnement
    suspend fun updateSubscription(subscription: Subscription) {
        subscriptionDao.updateSubscription(subscription)
    }
}
