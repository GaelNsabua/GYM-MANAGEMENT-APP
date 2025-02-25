/**
 * Les models sont des classes de données qui définissent la structure
 * des informations que nous stockons dans notre base de données.
 */

package com.example.gym_management_app.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//La classe Payment représente un payement effectué par un membre de la salle de sport.
@Entity(tableName = "payments") //annotée avec @Entity pour être stockée dans la base de données.
data class Payment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int, // Référence au membre
    val amount: Double, //Montant payé
    val date: Long //Date du paiement
)

