package com.example.assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    var emailPlainText : EditText? = null;
    var passwordPlainText : EditText? = null;
    private lateinit var executor: Executor;
    private lateinit var biometricPrompt: BiometricPrompt;
    private lateinit var promptInfo: BiometricPrompt.PromptInfo;

    /*
    * init layout
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        var firebaseAuth = sharedPerference.getString("firebaseAuth", "");
        var firebasePw = sharedPerference.getString("firebasePw", "");
        auth = FirebaseAuth.getInstance();

        executor = ContextCompat.getMainExecutor(this);


        biometricPrompt = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence ){
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show();
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult){
                super.onAuthenticationSucceeded(result);
                auth.signInWithEmailAndPassword(firebaseAuth.toString(), firebasePw.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val editor = sharedPerference.edit();
                        editor.putString("email", firebaseAuth.toString());
                        editor.commit();
                        val mainIntent = Intent(applicationContext, MainActivity:: class.java);
                        startActivity(mainIntent);
                        finish()
                    }
                }
                Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            override fun onAuthenticationFailed(){
                super.onAuthenticationFailed();
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account passord")
            .build();



        if(firebaseAuth == ""){
            val biometricLoginButton = findViewById<Button>(R.id.bioLoginButton);
            biometricLoginButton.isEnabled = false;
        }
        var biometricLoginButton = findViewById<Button>(R.id.bioLoginButton);
        biometricLoginButton.setOnClickListener{
            biometricPrompt.authenticate(promptInfo);
        }
    }

    /*
    * check the biometric status (fingerprint)
    * */
    override fun onResume(){
        super.onResume();
        val loginBiometricTextView = findViewById<TextView>(R.id.loginBiometricTextView);
        val biometricManager = BiometricManager.from(this);
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> loginBiometricTextView.text = "App can authenticate using biometrics.";
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> loginBiometricTextView.text = "No biometric features available on this device.";
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> loginBiometricTextView.text = "Biometric features are currently unavailable.";
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> loginBiometricTextView.text = "Biometric features are not enrolled.";
        }
    }

    /*
    * Login when button clicked
    * */
    fun onLoginButtonClick(view: View){
        emailPlainText = this.findViewById(R.id.emailPlainText);
        passwordPlainText = this.findViewById(R.id.passwordPlainText);
        val email = this.emailPlainText?.text.toString();
        val password = this.passwordPlainText?.text.toString();
        val sharedPerference = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE);
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
                if(task.isSuccessful){
                    val editor = sharedPerference.edit();
                    editor.putString("email", email);
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 0
                        )
                    }
                    val builder = AlertDialog.Builder(this);
                    builder.setMessage("Do you want to use biometric to login?")
                        .setCancelable(false)
                        .setPositiveButton("Yes"){ diolog, id ->
                            editor.putString("firebaseAuth", email);
                            editor.putString("firebasePw", password);
                            editor.commit();
                            diolog.dismiss();
                            val intent = Intent(this, MainActivity:: class.java);
                            startActivity(intent);
                            finish()
                        }
                        .setNegativeButton("No"){ diolog, id ->
                            editor.commit();
                            diolog.dismiss();
                            val intent = Intent(this, MainActivity:: class.java);
                            startActivity(intent);
                            finish()
                        }
                    val alert = builder.create();
                    alert.show();
                }
        }.addOnFailureListener{ exception ->
            Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show();
        }
    }

    /*
    * Go to Register View when button clicked
    * */
    fun onLoginRegisterButtonClick(view: View){
        var intent = Intent(this, RegisterActivity:: class.java);
        startActivity(intent);
        finish();
    }
}