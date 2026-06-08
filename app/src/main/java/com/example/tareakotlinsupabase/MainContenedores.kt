package com.example.tareakotlinsupabase

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.tareakotlinsupabase.adapters.AlumnoAdapter
import com.example.tareakotlinsupabase.models.Alumno
import com.example.tareakotlinsupabase.models.Materia
import com.example.tareakotlinsupabase.services.SupabaseManager
import com.example.tareakotlinsupabase.utils.SupabaseErrorHandler
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MainContenedores : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_contenedores)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val actvMaterias = findViewById<AutoCompleteTextView>(R.id.actvListaMaterias)
        val actvListaNiveles = findViewById<AutoCompleteTextView>(R.id.actvListaNiveles)
        val lvAlumnos = findViewById<ListView>(R.id.lvAlumnos)

        actvListaNiveles.setOnItemClickListener { _, _, position, _ ->
            actvMaterias.setText("")
            val lstMaterias = ArrayList<String>()
            lifecycleScope.launch {
                try {
                    val listaMaterias = ArrayList(
                        SupabaseManager.client
                            .from("materias")
                            .select {
                                filter {
                                    eq("nivel", position + 1)
                                }
                                order("nombre", Order.ASCENDING)
                            }
                            .decodeList<Materia>()
                    )

                    for (materia in listaMaterias) {
                        lstMaterias.add(materia.nombre ?: "")
                    }
                } catch (e: RestException) {
                    SupabaseErrorHandler.show(this@MainContenedores, e)
                    lstMaterias.clear()
                } finally {
                    val adapter = ArrayAdapter(
                        this@MainContenedores,
                        android.R.layout.simple_spinner_dropdown_item,
                        lstMaterias
                    )
                    actvMaterias.setAdapter(adapter)
                }
            }
        }

        actvMaterias.setOnItemClickListener { _, _, _, _ ->
            lifecycleScope.launch {
                val lstAlumnos = try {
                    ArrayList(
                        SupabaseManager.client
                            .from("alumnos")
                            .select {
                                order("nombres", Order.ASCENDING)
                            }
                            .decodeList<Alumno>()
                    )
                } catch (e: RestException) {
                    SupabaseErrorHandler.show(this@MainContenedores, e)
                    ArrayList()
                }

                lvAlumnos.adapter = AlumnoAdapter(this@MainContenedores, lstAlumnos)
            }
        }
    }
}
