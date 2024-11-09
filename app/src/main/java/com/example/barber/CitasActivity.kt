package com.example.barber

import BarberiaDbActivity
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.barber.R

class CitasActivity : AppCompatActivity() {

    private lateinit var dbHelper: BarberiaDbActivity
    private lateinit var layoutCitas: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.citas_activity)

        dbHelper = BarberiaDbActivity(this)
        layoutCitas = findViewById(R.id.layout_citas)

        cargarCitas()
    }

    private fun cargarCitas() {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT Citas.id_cita, Usuarios.usuario, Citas.fecha, Citas.hora FROM Citas INNER JOIN Usuarios ON Citas.id_usuario = Usuarios.id_usuario", null)

        if (cursor.moveToFirst()) {
            do {
                val nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val idCita = cursor.getInt(cursor.getColumnIndexOrThrow("id_cita"))

                // Crear la vista para cada cita inflando item_cita.xml
                val citaView = LayoutInflater.from(this).inflate(R.layout.citas_activity, layoutCitas, false)

                val tvNombreUsuario = citaView.findViewById<TextView>(R.id.tv_nombre_usuario)
                val tvFechaHora = citaView.findViewById<TextView>(R.id.tv_fecha_hora)
                val btnCancelarCita = citaView.findViewById<Button>(R.id.btn_cancelar_cita)

                tvNombreUsuario.text = "Nombre del Usuario: $nombreUsuario"
                tvFechaHora.text = "Fecha y Hora: $fecha a las $hora"

                btnCancelarCita.setOnClickListener {
                    val alertDialog = AlertDialog.Builder(this)
                        .setTitle("Cancelar Cita")
                        .setMessage("¿Quieres cancelar tu hora?")
                        .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                            cancelarCita(idCita)
                        }
                        .setNegativeButton("No", null)
                        .create()

                    alertDialog.show()
                }

                layoutCitas.addView(citaView)

            } while (cursor.moveToNext())
        }

        cursor.close()
    }

    private fun cancelarCita(idCita: Int) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM Citas WHERE id_cita = ?", arrayOf(idCita))
        Toast.makeText(this, "Hora cancelada", Toast.LENGTH_SHORT).show()
        layoutCitas.removeAllViews() // Limpiar la vista
        cargarCitas() // Recargar las citas para reflejar los cambios
    }
}
