package com.drab.tienda.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.drab.tienda.R

class FirtsAppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_firts_app)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
        insets


    }
        //Este codigo arranca la pantalla
        val btMas = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_mas)
        btMas.setOnClickListener {
        val intent = Intent(this, AgregarTenis::class.java)
            startActivity(intent)
        }
        val imgMas = findViewById<ImageButton>(R.id.img_agregar)
        imgMas.setOnClickListener {
            val intent = Intent(this, AgregarTenis::class.java)
            startActivity(intent)
        }

    }
}