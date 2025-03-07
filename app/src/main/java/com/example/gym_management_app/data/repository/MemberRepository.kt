/**
 *Le repository sert d'interface entre les ViewModels et la base de données.
 * Il permet d'organiser proprement les accès aux données et d’assurer un bon découplage.
 */

package com.example.gym_management_app.data.repository

import com.example.gym_management_app.data.database.MemberDao
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Subscription
import kotlinx.coroutines.flow.Flow

class MemberRepository(private val memberDao: MemberDao) {

    // Récupère tous les membres
    fun getAllMembers(): Flow<List<Member>> = memberDao.getAllMembers()

    // Ajoute un membre
    suspend fun insertMember(member: Member) = memberDao.insert(member)

    // Supprime un membre
    suspend fun deleteMember(member: Member) = memberDao.delete(member)

    //Met à jour le statut d'un membre
    suspend fun updateMember(member: Member) = memberDao.updateMember(member)

    // Nouveaux accès aux statistiques
    suspend fun getTotalMembersCount(): Int = memberDao.getTotalMembersCount()

    suspend fun getActiveMembersCount(): Int = memberDao.getActiveMembersCount()

    // Récupère un membre spécifique par son ID
    suspend fun getMemberById(id: Int): Member? {
        return memberDao.getMemberById(id)
    }

    suspend fun getInactiveMembersCount(): Int =
        memberDao.getInactiveMembersCount()

    // Fonction pour vérifier l'unicité de subscriptionId
    suspend fun getMemberBySubscriptionId(subscriptionId: Int): Member? {
        return memberDao.getMemberBySubscriptionId(subscriptionId)
    }

}
