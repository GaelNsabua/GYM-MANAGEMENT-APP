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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MemberViewModel(private val repository: MemberRepository) : ViewModel() {

    // Liste des membres
    private val _members = MutableStateFlow<List<Member>>(emptyList())
    val members: StateFlow<List<Member>> = _members

    // Statistiques sur les membres
    private val _totalMembers = MutableStateFlow(0)
    val totalMembers: StateFlow<Int> = _totalMembers

    private val _activeMembers = MutableStateFlow(0)
    val activeMembers: StateFlow<Int> = _activeMembers

    init {
        loadMembers()
        loadMemberStatistics()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            repository.getAllMembers().collect { memberList ->
                _members.value = memberList
            }
        }
    }

    private fun loadMemberStatistics() {
        viewModelScope.launch {
            _totalMembers.value = repository.getTotalMembersCount()
            _activeMembers.value = repository.getActiveMembersCount()
        }
    }

    fun addMember(member: Member) {
        viewModelScope.launch {
            repository.insertMember(member)
            loadMemberStatistics() // Recharger les stats après ajout
        }
    }

    suspend fun getMemberBySubscriptionId(subscriptionId: Int): Member? {
        return repository.getMemberBySubscriptionId(subscriptionId)
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.deleteMember(member)
            loadMemberStatistics() // Recharger les stats après suppression
        }
    }

    fun updateMember(member: Member) {
        viewModelScope.launch {
            repository.updateMember(member)
            loadMemberStatistics()
        }
    }
}

