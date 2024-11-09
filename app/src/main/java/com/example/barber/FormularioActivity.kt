package com.example.barber

import BarberiaDbActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class FormularioActivity : AppCompatActivity() {

    private lateinit var dbHelper: BarberiaDbActivity
    private lateinit var btnSeleccionarFecha: Button
    private lateinit var btnSeleccionarHora: Button
    private lateinit var btnAgendarHora: Button
    private lateinit var etNombreUsuario: EditText
    private lateinit var tvFechaSeleccionada: TextView
    private lateinit var tvHoraSeleccionada: TextView

    private var fechaSeleccionada: String = ""
    private var horaSeleccionada: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formulario_activity)

        dbHelper = BarberiaDbActivity(this)

        btnSeleccionarFecha = findViewById(R.id.btn_seleccionar_fecha)
        btnSeleccionarHora = findViewById(R.id.btn_seleccionar_hora)
        btnAgendarHora = findViewById(R.id.btn_agendar)
        etNombreUsuario = findViewById(R.id.et_nombre_usuario)
        tvFechaSeleccionada = findViewById(R.id.tv_fecha_seleccionada)
        tvHoraSeleccionada = findViewById(R.id.tv_hora_seleccionada)

        btnSeleccionarFecha.setOnClickListener {
            mostrarDatePicker()
        }

        btnSeleccionarHora.setOnClickListener {
            mostrarTimePicker()
        }

        btnAgendarHora.setOnClickListener {
            if (etNombreUsuario.text.toString().isEmpty() || fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                guardarCitaEnBaseDeDatos()
            }
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            fechaSeleccionada = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            tvFechaSeleccionada.text = "Fecha: $fechaSeleccionada"
        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            horaSeleccionada = "$selectedHour:$selectedMinute"
            tvHoraSeleccionada.text = "Hora: $horaSeleccionada"
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun guardarCitaEnBaseDeDatos() {
        val db = dbHelper.writableDatabase
        val nombreUsuario = etNombreUsuario.text.toString().trim()

        val cursor = db.rawQuery("SELECT id_usuario FROM Usuarios WHERE usuario = ?", arrayOf(nombreUsuario))

        if (cursor.moveToFirst()) {
            val idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario"))

            val insertQuery = "INSERT INTO Citas (id_usuario, fecha, hora) VALUES (?, ?, ?)"
            val stmt = db.compileStatement(insertQuery)
            stmt.bindLong(1, idUsuario.toLong())
            stmt.bindString(2, fechaSeleccionada)
            stmt.bindString(3, horaSeleccionada)

            try {
                stmt.executeInsert()
                Toast.makeText(this, "Hora agendada exitosamente", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error al agendar la cita: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("FormularioActivity", "Error al agendar la cita: ${e.message}")
            }
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
            Log.e("FormularioActivity", "Usuario no encontrado en la base de datos.")
        }

        cursor.close()
    }
}
