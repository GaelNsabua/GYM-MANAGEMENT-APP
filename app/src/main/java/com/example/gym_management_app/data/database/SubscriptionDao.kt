/**
 * Les DAO sont les interfaces qui permettent d’accéder aux données stockées dans la base de données.
 * Elles définissent les opérations possibles sur les entités (CRUD : Create, Read, Update, Delete).
 */

package com.example.gym_management_app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gym_management_app.data.models.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    // Insère un abonnement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscription: Subscription)

    // Supprime un abonnement
    @Delete
    suspend fun delete(subscription: Subscription)

    // Récupère tous les abonnements
    @Query("SELECT * FROM subscriptions ORDER BY endDate DESC")
    fun getAllSubscriptions(): Flow<List<Subscription>>

    // Rechercher un abonnement par son ID
    @Query("SELECT * FROM subscriptions WHERE id = :id")
    suspend fun getSubscriptionById(id: Int): Subscription?

    //Mettre à jour la date de fin de l'abonnement
    @Update
    suspend fun updateSubscription(subscription: Subscription)

    @Query("SELECT COUNT(*) FROM subscriptions WHERE endDate <= :currentTime")
    suspend fun getExpiredSubscriptionsCount(currentTime: Long = System.currentTimeMillis()): Int
}
