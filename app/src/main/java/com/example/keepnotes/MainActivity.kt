package com.example.keepnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user: MutableMap<String, Any> = HashMap()
        user["first"] = "Konstantyn"
        user["last"] = "Huzil"
        user["born"] = 2006

        val note: Note = Note("a", "b", "c", Date())

        FirebaseFirestore.getInstance().collection("users")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error adding document", e)
            }
    }
}