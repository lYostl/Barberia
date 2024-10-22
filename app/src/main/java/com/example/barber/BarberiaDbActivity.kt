import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BarberiaDbActivity(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla Usuarios
        val CREATE_USUARIOS_TABLE = "CREATE TABLE Usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT UNIQUE NOT NULL," +
                "correo TEXT NOT NULL," +
                "contrasena TEXT NOT NULL);"
        db.execSQL(CREATE_USUARIOS_TABLE)

        // Crear tabla Barbero
        val CREATE_BARBERO_TABLE = "CREATE TABLE Barbero (" +
                "id_barbero INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usuario TEXT UNIQUE NOT NULL," +
                "contrasena TEXT NOT NULL);"
        db.execSQL(CREATE_BARBERO_TABLE)

        // Insertar barbero por defecto
        val INSERT_BARBERO =
            "INSERT INTO Barbero (usuario, contrasena) VALUES ('barbero', 'hola123');"
        db.execSQL(INSERT_BARBERO)

        // Crear tabla Citas
        val CREATE_CITAS_TABLE = "CREATE TABLE Citas (" +
                "id_cita INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_usuario INTEGER NOT NULL," +
                "fecha TEXT NOT NULL," +
                "hora TEXT NOT NULL," +
                "FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario));"
        db.execSQL(CREATE_CITAS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios")
        db.execSQL("DROP TABLE IF EXISTS Barbero")
        db.execSQL("DROP TABLE IF EXISTS Citas")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "Barberia.db"
        private const val DATABASE_VERSION = 1
    }
}