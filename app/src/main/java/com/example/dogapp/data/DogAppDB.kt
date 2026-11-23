package com.example.dogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dogapp.model.Cita
import com.example.dogapp.utils.Constants

@Database(entities = [Cita::class], version = 1, exportSchema = false)
abstract class DogAppDB : RoomDatabase() {
    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile
        private var INSTANCE: DogAppDB? = null

        fun getDatabase(context: Context): DogAppDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DogAppDB::class.java,
                    Constants.NAME_BD
                ).build().also { INSTANCE = it }
            }
    }
}
