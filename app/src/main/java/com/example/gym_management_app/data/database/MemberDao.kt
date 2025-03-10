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
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Subscription
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    // Insère un nouveau membre
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(member: Member)

    // Supprime un membre
    @Delete
    suspend fun delete(member: Member)

    // Récupère tous les membres
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    // Récupérer un membre par subscriptionId
    @Query("SELECT * FROM members WHERE subscriptionId = :subscriptionId LIMIT 1")
    suspend fun getMemberBySubscriptionId(subscriptionId: Int): Member?

    //Retourne tous les membres inactifs
    @Query("SELECT COUNT(*) FROM members WHERE endDate <= :currentTime")
    suspend fun getInactiveMembersCount(currentTime: Long = System.currentTimeMillis()): Int

    // Rechercher un abonnement par son ID
    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Int): Member?

    //Mettre à jour le statut d'un membre
    @Update
    suspend fun updateMember(member: Member)

    //Retourne le nombre des membres inscrits
    @Query("SELECT COUNT(*) FROM members")
    suspend fun getTotalMembersCount(): Int

    //Retourne le nombre des membres actifs
    @Query("SELECT COUNT(*) FROM members WHERE isActive = 1")
    suspend fun getActiveMembersCount(): Int
}
