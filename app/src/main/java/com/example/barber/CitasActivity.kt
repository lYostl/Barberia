package com.example.barber

import BarberiaDbActivity
import android.content.DialogInterface
import android.database.Cursor
import android.os.Bundle
import android.view.View
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
        val cursor: Cursor = db.rawQuery(
            "SELECT Citas.id_cita, Usuarios.usuario, Citas.fecha, Citas.hora FROM Citas INNER JOIN Usuarios ON Citas.id_usuario = Usuarios.id_usuario",
            null
        )

        if (cursor.moveToFirst()) {
            layoutCitas.visibility = View.VISIBLE

            do {
                val nombreUsuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                val hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"))
                val idCita = cursor.getInt(cursor.getColumnIndexOrThrow("id_cita"))

                // Crear programáticamente la vista para cada cita
                val citaView = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(32, 32, 32, 32)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(16, 16, 16, 32)
                    }
                    setBackgroundResource(R.drawable.card_background)
                }


                val tvNombreUsuario = TextView(this).apply {
                    text = "Nombre: $nombreUsuario"
                    textSize = 24f // Hacer el texto un poco más grande
                    setTextColor(resources.getColor(R.color.white, null))
                    setPadding(0, 0, 0, 16)
                }


                val tvFechaHora = TextView(this).apply {
                    text = "Fecha y Hora: $fecha a las ${formatHora(hora)}"
                    textSize = 18f // Aumentar el tamaño del texto de la fecha
                    setTextColor(resources.getColor(R.color.grey, null))
                    setPadding(0, 0, 0, 16)
                }

                // Crear el botón para cancelar la cita
                val btnCancelarCita = Button(this).apply {
                    text = "CANCELAR"
                    textSize = 14f // Reducir el tamaño del texto del botón
                    setTextColor(resources.getColor(R.color.white, null))
                    setBackgroundColor(resources.getColor(R.color.red, null))
                    setPadding(8, 8, 8, 8)
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 16, 0, 0)
                    }
                    setOnClickListener {
                        val alertDialog = AlertDialog.Builder(this@CitasActivity)
                            .setTitle("Cancelar Cita")
                            .setMessage("¿Quieres cancelar tu hora?")
                            .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                                cancelarCita(idCita)
                            }
                            .setNegativeButton("No", null)
                            .create()

                        alertDialog.show()
                    }
                }


                citaView.addView(tvNombreUsuario)
                citaView.addView(tvFechaHora)
                citaView.addView(btnCancelarCita)


                layoutCitas.addView(citaView)

            } while (cursor.moveToNext())
        } else {
            layoutCitas.visibility = View.GONE
            Toast.makeText(this, "No hay citas programadas", Toast.LENGTH_SHORT).show()
        }

        cursor.close()
    }

    private fun cancelarCita(idCita: Int) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM Citas WHERE id_cita = ?", arrayOf(idCita))
        Toast.makeText(this, "Hora cancelada", Toast.LENGTH_SHORT).show()
        layoutCitas.removeAllViews()
        cargarCitas()
    }


    private fun formatHora(hora: String): String {
        val partes = hora.split(":")
        val horaFormateada = partes[0].padStart(2, '0')
        val minutosFormateados = if (partes.size > 1) partes[1].padStart(2, '0') else "00"
        return "$horaFormateada:$minutosFormateados"
    }
}
