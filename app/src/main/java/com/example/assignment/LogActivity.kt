package com.example.assignment

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.assignment.databinding.ActivityLogBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime

class LogActivity : AppCompatActivity() {

    lateinit var _db:DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log);

        _db = FirebaseDatabase.getInstance().reference;
    }

    fun addTask(view: View){
        val task = Task.create();

        val newResultPlainText = findViewById<EditText>(R.id.ResultPlainText);
        task.createDate = LocalDateTime.now().toString();
        task.number = newResultPlainText.text.toString();

        val newResult = _db.child(Statics.FIREBASE_TASK).push();
        task.objectId = newResult.key;

        newResult.setValue(task);

        newResultPlainText.setText("");

        Toast.makeText(this, "The result added to the list", Toast.LENGTH_SHORT).show();

    }
}