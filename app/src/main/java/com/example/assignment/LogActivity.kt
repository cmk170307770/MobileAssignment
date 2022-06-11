package com.example.assignment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.assignment.databinding.ActivityLogBinding
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LogActivity : AppCompatActivity() {

    lateinit var _db:DatabaseReference;
    lateinit var _adapter: TaskAdapter;
    var _taskList:MutableList<Task>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log);

        _taskList = mutableListOf();
        _db = FirebaseDatabase.getInstance().reference;
        _adapter = TaskAdapter(this, _taskList!!);
        val listviewTask = findViewById<ListView>(R.id.listViewResult);
        listviewTask!!.setAdapter(_adapter);

        _db.orderByKey().addValueEventListener(_taskListener);
    }

    var _taskListener: ValueEventListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            loadTaskList(snapshot);
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("LogActivity", "loadItem:onCancelled", error.toException());
        }
    }

    private fun loadTaskList(dataSnapshot: DataSnapshot){
        val tasks = dataSnapshot.children.iterator();

        if(tasks.hasNext()){
            _taskList!!.clear();

            var listIndex = tasks.next();
            val itemsIterator = listIndex.children.iterator();

            while (itemsIterator.hasNext()){
                val currentItem = itemsIterator.next();
                val task = Task.create();

                var map = currentItem.getValue() as HashMap<String, Any>;

                task.objectId = currentItem.key;
                var createDateTime = LocalDateTime.parse(map.get("createDate") as String);
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val formatted = createDateTime.format(formatter)
                task.createDate = formatted;
                task.number = map.get("number") as String;
                _taskList!!.add(task);
            }
        }
        _adapter.notifyDataSetChanged();
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