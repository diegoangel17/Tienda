package com.drab.tienda.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.drab.tienda.R
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import com.drab.tienda.data.ModeloBase
import com.drab.tienda.data.TeniDao
import com.drab.tienda.data.TenisData
import com.drab.tienda.tools.Camera
import kotlinx.coroutines.launch


class AgregarTenis : AppCompatActivity() {

    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var db: TenisData
    private lateinit var teniDao: TeniDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_tenis)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = TenisData.getDatabase(applicationContext)
        teniDao = db.teniDao()

        // Registrar el contrato para recibir resultados
        cameraResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUrl = result.data?.getStringExtra("IMAGE_URL") ?: ""
                val imageView = findViewById<ImageView>(R.id.seleccion)
                imageView.setImageURI(Uri.parse(imageUrl))
            }
        }
        // Registrar el contrato para seleccionar imágenes de la galería
        galleryResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    val imageView = findViewById<ImageView>(R.id.seleccion)
                    imageView.setImageURI(it)
                }
            }
        }

        val btnCamera = findViewById<ImageButton>(R.id.camara)
        btnCamera.setOnClickListener {
            val intent = Intent(this, Camera::class.java)
            cameraResultLauncher.launch(intent) // Usar el lanzador en lugar de startActivityForResult
        }
        val btnSelectFromGallery = findViewById<ImageButton>(R.id.slcImg)
        btnSelectFromGallery.setOnClickListener {
            openGallery()
        }

        val nombreC=findViewById<AppCompatEditText>(R.id.Campo_txt1)
        val precioC=findViewById<AppCompatEditText>(R.id.Campo_txt2)
        val guardar=findViewById<ImageButton>(R.id.Guardar)

        val imageUrl = intent.getStringExtra("IMAGE_URL") ?: ""
        val imageView = findViewById<ImageView>(R.id.seleccion)
        imageView.setImageURI(Uri.parse(imageUrl))

        // Referencia al Spinner
        val desplegable1= findViewById<Spinner>(R.id.spinner_1)
        val desplegable2=findViewById<Spinner>(R.id.spinner_2)
        val desplegable3=findViewById<Spinner>(R.id.spinner_3)
        //Tallas
        val checkBox = listOf(
            findViewById(R.id.T1),
            findViewById(R.id.T2),
            findViewById(R.id.T3),
            findViewById(R.id.T4),
            findViewById(R.id.T5),
            findViewById<CheckBox>(R.id.T6)
        )
        

        // Crear el adaptador con las opciones

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_options,
            R.layout.spinner_item,// Array de strings definido en strings.xml

        )
        val adapter2 = ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_options2,
            R.layout.spinner_item,

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        desplegable1.adapter = adapter
        desplegable2.adapter = adapter
        desplegable3.adapter = adapter2

        desplegable3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOption = parent?.getItemAtPosition(position).toString()

                // Actualizar texto de RadioButtons según selección
                when (selectedOption) {
                    "12/14.5" -> {
                        val values = listOf("12", "12.5", "13", "13.5", "14", "14.5")
                        for (i in checkBox.indices) {
                            checkBox[i].text = values[i]
                        }
                    }
                    "15/17.5" -> {
                        val values = listOf("15", "15.5", "16", "16.5", "17", "17.5")
                        for (i in checkBox.indices) {
                            checkBox[i].text = values[i]
                        }
                    }
                    "18.5/21" -> {
                        val values = listOf("18.5", "19", "19.5", "20", "20.5", "21")
                        for (i in checkBox.indices) {
                            checkBox[i].text = values[i]
                        }
                    }

                    "22.5/25" -> {
                        val values = listOf("22.5", "23", "23.5", "24", "24.5", "25")
                        for (i in checkBox.indices) {
                            checkBox[i].text = values[i]
                        }
                    }

                    "25.5/28" -> {
                        val values = listOf("25.5", "26", "26.5", "27", "27.5", "28")
                        for (i in checkBox.indices) {
                            checkBox[i].text = values[i]
                        }
                    }
                }
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Acciones cuando no se selecciona nada

            }
        }

        guardar.setOnClickListener {
            val nombre = nombreC.text.toString()
            val precio = precioC.text.toString().toDouble()
            val color1 = desplegable1.selectedItem.toString()
            val color2 = desplegable2.selectedItem.toString()
            val tallas = desplegable3.selectedItem.toString()
            val numeracion: MutableList<Boolean> = mutableListOf()
            for (i in checkBox.indices){
                numeracion.add(checkBox[i].isChecked)
            }

            if (nombre.isNotEmpty() && color1.isNotEmpty()) {
                lifecycleScope.launch {
                    teniDao.insertTeni(ModeloBase(nombre = nombre, precio = precio, color1 = color1, color2 = color2, tallas = tallas, t1 = numeracion[0], t2 = numeracion[1], t3 = numeracion[2], t4 = numeracion[3], t5 = numeracion[4], t6 = numeracion[5], imagen = imageUrl))
                    Log.e("Guardado", "Se guardaron los datos")
                    finish()
                }
            }
            else{
                Log.e("Guardado", "No se guardo nada")

            }
        }

    }
    //Aqui las funciones privadas
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryResultLauncher.launch(intent)
    }

}