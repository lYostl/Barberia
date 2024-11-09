package com.example.barber

import BarberiaDbActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent


class RegistroActivity : AppCompatActivity() {
    lateinit var usuarioInput: EditText
    lateinit var correoInput: EditText
    lateinit var contrasenaInput: EditText
    lateinit var registrarBtn: Button
    lateinit var volverLoginBtn: Button
    private lateinit var dbHelper: BarberiaDbActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registros_activity)

        usuarioInput = findViewById(R.id.et_usuario_registro)
        correoInput = findViewById(R.id.et_correo_registro)
        contrasenaInput = findViewById(R.id.et_contrasena_registro)
        registrarBtn = findViewById(R.id.btn_registrar)
        volverLoginBtn = findViewById(R.id.btn_volver_login)
        dbHelper = BarberiaDbActivity(this)

        registrarBtn.setOnClickListener {
            val usuario = usuarioInput.text.toString()
            val correo = correoInput.text.toString()
            val contrasena = contrasenaInput.text.toString()

            if (usuario.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                registrarUsuario(usuario, correo, contrasena)
            }
        }

        volverLoginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para evitar volver con el botón 'atrás'
        }
    }

    private fun registrarUsuario(usuario: String, correo: String, contrasena: String) {
        val db = dbHelper.writableDatabase
        val insertQuery = "INSERT INTO Usuarios (usuario, correo, contrasena) VALUES (?, ?, ?)"

        try {
            db.execSQL(insertQuery, arrayOf(usuario, correo, contrasena))
            Toast.makeText(this, "SE HA REGISTRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()
            finish() // Regresar al login automáticamente después del registro exitoso
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}