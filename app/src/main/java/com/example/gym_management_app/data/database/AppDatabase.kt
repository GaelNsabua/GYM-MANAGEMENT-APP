package com.example.gym_management_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Payment
import com.example.gym_management_app.data.models.Subscription

@Database(entities = [Member::class, Subscription::class, Payment::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_management_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}