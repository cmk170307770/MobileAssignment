package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    var emailPlainText : EditText? = null;
    var passwordPlainText : EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
    }

    fun onLoginButtonClick(view: View){
        emailPlainText = this.findViewById(R.id.emailPlainText);
        passwordPlainText = this.findViewById(R.id.passwordPlainText);
        val email = this.emailPlainText?.text.toString();
        val password = this.passwordPlainText?.text.toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
                if(task.isSuccessful){
                    val intent = Intent(this, MainActivity:: class.java);
                    startActivity(intent);
                    finish()
                }
        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show();

        }

    }
}