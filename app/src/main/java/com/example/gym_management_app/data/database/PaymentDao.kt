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
import com.example.gym_management_app.data.models.Payment
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    // Insère un paiement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: Payment)

    // Supprime un paiement
    @Delete
    suspend fun delete(payment: Payment)

    // Récupère tous les paiements
    @Query("SELECT * FROM payments ORDER BY date DESC")
    fun getAllPayments(): Flow<List<Payment>>

    // Rechercher un payement par son ID
    @Query("SELECT * FROM payments WHERE memberId = :memberId")
    fun getPaymentsForMember(memberId: Int): Flow<List<Payment>>

    @Query("SELECT SUM(amount) FROM payments WHERE date BETWEEN :start AND :end")
    suspend fun getMonthlyRevenue(start: Long, end: Long): Double?
}
