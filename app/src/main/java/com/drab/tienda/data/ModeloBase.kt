package com.drab.tienda.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "teni")
data class ModeloBase (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val color1: String,
    val color2: String,
    val tallas: String,
    val t1: Boolean,
    val t2: Boolean,
    val t3: Boolean,
    val t4: Boolean,
    val t5: Boolean,
    val t6: Boolean,
    val imagen: String
)