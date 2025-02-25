/**
 * Les models sont des classes de données qui définissent la structure
 * des informations que nous stockons dans notre base de données.
 */

package com.example.gym_management_app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//La classe Member représente un membre de la salle de sport.
@Entity(tableName = "members") //annotée avec @Entity pour être stockée dans la base de données.
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val contact: String,
    val subscriptionId: Int, // Référence à l’abonnement
    val isActive: Boolean = true
)

