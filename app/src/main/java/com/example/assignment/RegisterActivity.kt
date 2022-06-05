package com.example.assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    var registerEmailPlainText : EditText? = null;
    var registerPasswordPlainText : EditText? = null;

    public override fun onStart(){
        super.onStart();
        val currentUser = auth.currentUser;
        if(currentUser != null){
            //reload();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
    }

    fun onRegisterButtonClick(view: View){
        registerEmailPlainText = this.findViewById(R.id.registerEmailPlainText);
        registerPasswordPlainText = this.findViewById(R.id.registerPasswordPlainText);

        var email = this.registerEmailPlainText?.text.toString();
        var password = this.registerPasswordPlainText?.text.toString();

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                var intent = Intent(this,LoginActivity::class.java);
                startActivity(intent);
                finish();
            }
        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
        };
    }

    fun onLoginButtonClick(view: View){
        var intent = Intent(this, LoginActivity::class.java);
        startActivity(intent);
    }
}