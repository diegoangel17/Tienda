package com.drab.tienda.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [ModeloBase::class], version = 1, exportSchema = false)
abstract class TenisData:RoomDatabase() {
    abstract fun teniDao():TeniDao
    companion object {
        @Volatile
        private var INSTANCE: TenisData? = null

        fun getDatabase(context: Context): TenisData {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TenisData::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}