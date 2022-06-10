package com.example.assignment

class Task {
    companion object Factory{
        fun create(): Task = Task();
    }

    var objectId: String? = null;
    var createDate: String? = null;
    var number: String? = null;
}