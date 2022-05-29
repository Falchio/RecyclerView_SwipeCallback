package com.github.falchio.recyclerviewswipecallback

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.github.falchio.recyclerviewswipecallback.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val itemCallback = ItemCallback()
        val touchHelper = ItemTouchHelper(itemCallback)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.adapter = CustomAdapter(Data.get())
    }
}