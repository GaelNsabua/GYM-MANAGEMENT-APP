/**
 * Les models sont des classes de données qui définissent la structure
 * des informations que nous stockons dans notre base de données.
 */

package com.example.gym_management_app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//La classe Subscription représente un abonnement à la salle de sport.
@Entity(tableName = "subscriptions") //annotée avec @Entity pour être stockée dans la base de données.
data class Subscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // Ex : "Mensuel", "Annuel"
    val description: String,
    val price: Double //Le prix de l'abonnement
)

