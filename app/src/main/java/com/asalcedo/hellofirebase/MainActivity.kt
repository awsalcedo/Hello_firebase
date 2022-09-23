package com.asalcedo.hellofirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.asalcedo.hellofirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get reference
        val database = Firebase.database.reference
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = snapshot.getValue(String::class.java)
                    binding.tvData.text = "Firebase Remote: $data"
                } else {
                    binding.tvData.text = "Ruta sin datos."
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al leer datos", Toast.LENGTH_SHORT).show()
            }

        }

        //Get the path of the tree found in the RealTime Database
        val dataRef = database.child("hola_firebase").child("data")
        dataRef.addValueEventListener(listener)

        //Write in RealTime Database
        binding.btnSend.setOnClickListener {
            val data = binding.etData.text.toString()
            dataRef.setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this@MainActivity, "Enviado...", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, "Error al enviar", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    Toast.makeText(this@MainActivity, "Terminado", Toast.LENGTH_SHORT).show()
                }
        }

        //Delete register in RealTime Database
        binding.btnSend.setOnLongClickListener {
            dataRef.removeValue()
            true
        }

    }
}