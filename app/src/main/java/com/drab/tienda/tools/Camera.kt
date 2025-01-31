package com.drab.tienda.tools

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import com.drab.tienda.R
import java.io.File
import java.util.Locale

class Camera : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraProvider: ProcessCameraProvider

    // Lanzador para la selección de imágenes de la galería
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!requiredPermissions()) {
            requestPermissions(cameraPermisos, 0)
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_camera)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar cámara
        startCamera()

        // Botón para tomar una foto
        val takePhoto = findViewById<Button>(R.id.takePhoto)
        takePhoto.setOnClickListener {
            takePhoto()
        }

        // Botón para abrir la galería
        val selectFromGallery = findViewById<Button>(R.id.btnOpenGallery)
        selectFromGallery.setOnClickListener {
            openGallery()
        }

        // Inicializar el lanzador de galería
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = result.data?.data
                selectedImageUri?.let {
                    goToImageScreen(it.toString())
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // Configura la vista previa
            val preview = Preview.Builder().build()
            val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder)
            preview.surfaceProvider = previewView.surfaceProvider

            // Configura la captura de imágenes
            imageCapture = ImageCapture.Builder().build()

            // Selecciona la cámara trasera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Desvincula cualquier uso anterior de la cámara
                cameraProvider.unbindAll()

                // Vincula la cámara al ciclo de vida de esta actividad
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraX", "Error al iniciar la cámara", exc)
            }
            
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Crea un archivo para guardar la foto
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e("CameraX", "Error al capturar la imagen: ${exc.message}", exc)
                    Toast.makeText(this@Camera, "Error al capturar la imagen", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    goToImageScreen(savedUri.toString())
                }
            }
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }

    private fun goToImageScreen(imageUrl: String) {
        val resultIntent = Intent().apply {
            putExtra("IMAGE_URL", imageUrl)
        }
        setResult(RESULT_OK, resultIntent) // Enviar el resultado
        finish() // Finalizar la actividad Camera
    }

    private fun requiredPermissions(): Boolean {
        return cameraPermisos.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera la cámara al destruir la actividad
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    companion object {
        private val cameraPermisos = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}
