package com.example.dogapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dogapp.model.Cita

@Dao
interface CitaDao {
    /** LiveData permite que Home observe cambios después de crear, editar o borrar citas. */
    @Query("SELECT * FROM citas ORDER BY id ASC")
    fun observarCitas(): LiveData<List<Cita>>

    @Query("SELECT * FROM citas WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Cita?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(cita: Cita): Long

    @Update
    suspend fun actualizar(cita: Cita)

    @Delete
    suspend fun eliminar(cita: Cita)
}
