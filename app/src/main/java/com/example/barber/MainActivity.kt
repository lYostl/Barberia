package com.example.barber

import BarberiaDbActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var usuarioInput: EditText
    lateinit var passwordInput: EditText
    lateinit var loginBtn: Button
    lateinit var instagramBtn: ImageView
    lateinit var registroBtn: Button
    private lateinit var dbHelper: BarberiaDbActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usuarioInput = findViewById(R.id.et_usuario)
        passwordInput = findViewById(R.id.et_contrasena)
        loginBtn = findViewById(R.id.btn_acceder)
        instagramBtn = findViewById(R.id.iv_instagram)
        registroBtn = findViewById(R.id.btn_registro)
        dbHelper = BarberiaDbActivity(this)

        loginBtn.setOnClickListener {
            val usuario = usuarioInput.text.toString()
            val contrasena = passwordInput.text.toString()

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                iniciarSesion(usuario, contrasena)
            }
        }

        instagramBtn.setOnClickListener {
            val uri = Uri.parse("https://www.instagram.com/bad_boys_oficial7/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        registroBtn.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion(usuario: String, contrasena: String) {
        val db = dbHelper.readableDatabase

        // Verificar si es el barbero (administrador)
        var cursor = db.rawQuery(
            "SELECT * FROM Barbero WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, contrasena)
        )
        if (cursor.moveToFirst()) {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            cursor.close()
            return
        }
        cursor.close()

        // Verificar si es un usuario registrado
        cursor = db.rawQuery(
            "SELECT * FROM Usuarios WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, contrasena)
        )
        if (cursor.moveToFirst()) {
            val intent = Intent(this, FormularioActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
    }
}
