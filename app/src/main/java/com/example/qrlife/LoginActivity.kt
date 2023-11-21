package com.example.qrlife

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrlife.databinding.LoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var loginIntent: Intent
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.textSignUp.setOnClickListener { onSignUpClicked() }
        binding.buttonLogin.setOnClickListener { onButtonLoginClicked() }
    }

    private fun onSignUpClicked() {
        loginIntent = Intent(this, SignUpActivity::class.java)
        startActivity(loginIntent)
    }

    private fun onButtonLoginClicked() {
        val enteredEmail = binding.editTextEmail.text.toString()
        val enteredPassword = binding.editTextPassword.text.toString()

        // Vérifier si les champs d'adresse e-mail et de mot de passe ne sont pas vides
        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Utiliser Firebase Auth pour la connexion
        auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Connexion réussie, rediriger vers MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Gérer les erreurs de connexion
                    Toast.makeText(this, "Échec de la connexion", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
