package com.moviles2025.freshlink43.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.moviles2025.freshlink43.R
import com.moviles2025.freshlink43.ui.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Ejemplo básico de un botón que te lleva al perfil (solo para que veas flujo)
        val profileButton = findViewById<Button>(R.id.profileButton)

        profileButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Aquí luego puedes observar LiveData de restaurantes, etc.
        viewModel.welcomeMessage.observe(this) { message ->
            // Mostrar mensaje de bienvenida (opcional)
            println(message)
        }
    }
}