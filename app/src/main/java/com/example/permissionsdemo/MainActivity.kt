package com.example.permissionsdemo

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest

class MainActivity : AppCompatActivity() {
    private val cameraResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        run {
            if (isGranted) Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG)
                .show()
            else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted) {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG)
                            .show()
                    } else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        Toast.makeText(
                            this, "Coarse location permission granted", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this, "Camera permission granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show()
                    } else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        Toast.makeText(this, "Coarse Location permission denied", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCameraPermission: Button = findViewById(R.id.btnCameraPermission)
        btnCameraPermission.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                rationalDialog(
                    "Permission Demo requires the Camera access",
                    "Camera cannot be used because camera access is denied"
                )
            } else {
                cameraLocationResultLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun rationalDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}