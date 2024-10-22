package com.example.barber

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    lateinit var misCitasBtn: Button
    lateinit var cerrarSesionBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_activity)

        misCitasBtn = findViewById(R.id.btn_ver_reservas)
        cerrarSesionBtn = findViewById(R.id.btn_cerrar_sesion)

        misCitasBtn.setOnClickListener {
            val intent = Intent(this, CitasActivity::class.java)
            startActivity(intent)
        }

        cerrarSesionBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para evitar volver con el botón 'atrás'
        }
    }
}
