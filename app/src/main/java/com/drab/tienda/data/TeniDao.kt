package com.drab.tienda.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface TeniDao {


        @Insert
        suspend fun insertTeni(teni:ModeloBase)

        @Query("SELECT imagen FROM teni")
        suspend fun getAllUrl(): List<String>

        @Query("SELECT imagen FROM teni WHERE nombre = :nombre LIMIT 1")
        suspend fun getImageUrlByName(nombre: String): String?

        @Query("DELETE FROM teni")
        suspend fun deleteAllTenis()

        @Query("DELETE FROM sqlite_sequence")
        suspend fun deleteIds()
}