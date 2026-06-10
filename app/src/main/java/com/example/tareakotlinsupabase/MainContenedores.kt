package com.example.tareakotlinsupabase

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
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

    private lateinit var spnMaterias: Spinner
    private lateinit var lvAlumnos: ListView
    private var ignorarMateriaInicial = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_contenedores)

        val spnSemestre = findViewById<Spinner>(R.id.spnSemestre)
        spnMaterias = findViewById(R.id.spnMaterias)
        lvAlumnos = findViewById(R.id.lvAlumnos)

        spnMaterias.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("Selecciona una materia")
        )

        spnSemestre.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.niveles)
        )

        spnSemestre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargarMaterias(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        spnMaterias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (ignorarMateriaInicial) {
                    ignorarMateriaInicial = false
                    return
                }
                if (position == 0) return
                cargarAlumnos()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun cargarMaterias(nivel: Int) {
        lifecycleScope.launch {
            val lstMaterias = ArrayList<String>()
            try {
                val listaMaterias = SupabaseManager.client
                    .from("materias")
                    .select {
                        filter { eq("nivel", nivel) }
                        order("nombre", Order.ASCENDING)
                    }
                    .decodeList<Materia>()

                lstMaterias.add("Selecciona una materia")
                for (materia in listaMaterias) {
                    lstMaterias.add(materia.nombre ?: "")
                }
            } catch (e: RestException) {
                SupabaseErrorHandler.show(this@MainContenedores, e)
            } finally {
                ignorarMateriaInicial = true
                spnMaterias.adapter = ArrayAdapter(
                    this@MainContenedores,
                    android.R.layout.simple_spinner_dropdown_item,
                    lstMaterias
                )
            }
        }
    }

    private fun cargarAlumnos() {
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
