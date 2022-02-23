package edu.singaporetech.firstapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.singaporetech.firstapp.databinding.ActivitySignBinding


class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create View Binding
        binding = ActivitySignBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //TODO: add your logging message here.

    }
}