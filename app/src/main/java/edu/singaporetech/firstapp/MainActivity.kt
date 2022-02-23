package edu.singaporetech.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import edu.singaporetech.firstapp.databinding.ActivityMainBinding

/**
 * Lab 01: My First App
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setContentView(R.layout.activity_sign)

        //TODO: add your logging message here.
    }
}
