// SignUpActivity.kt
package com.example.qrlife

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qrlife.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpBinding
    private lateinit var signUpIntent: Intent
    private lateinit var auth: FirebaseAuth


    companion object {
        private const val TAG = "SignUpActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.textLogin.setOnClickListener { onLoginClicked() }
        binding.buttonSignUp.setOnClickListener { onSignUpClicked() }

    }

    fun onLoginClicked(){
        signUpIntent = Intent(this, LoginActivity::class.java)
        startActivity(signUpIntent)
    }

    private fun onSignUpClicked() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Utilisation de Firebase Auth pour créer un nouvel utilisateur
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user: FirebaseUser? = auth.currentUser
                    // Mettez à jour l'interface utilisateur ou effectuez d'autres actions nécessaires
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        // Mettez à jour l'interface utilisateur en fonction de l'état de l'authentification
        if (user != null) {
            // L'utilisateur est connecté, vous pouvez rediriger vers une autre activité
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // L'inscription a échoué ou l'utilisateur n'est pas connecté
            // Peut-être mettre à jour l'interface utilisateur en conséquence
        }
    }
}
