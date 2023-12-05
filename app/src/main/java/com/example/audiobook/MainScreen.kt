package com.example.audiobook

import android.os.Bundle
import android.widget.TextView

import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.audiobook.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = binding.navView
        textView = binding.title



        val navController = findNavController(R.id.nav_host_fragment_activity_main_screen)

        // Thiết lập text cho title ban đầu
        updateTitle(navController.currentDestination?.id)

        // Thêm listener để cập nhật title khi chuyển đổi fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateTitle(destination.id)
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_search, R.id.navigation_music,R.id.navigation_profile
//            )
//        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    private fun updateTitle(destinationId: Int?) {
        val titleResId = when (destinationId) {
            R.id.navigation_home -> R.string.title_home
            R.id.navigation_search -> R.string.title_search
            R.id.navigation_music -> R.string.title_music
            R.id.navigation_profile -> R.string.title_profile
            else -> R.string.app_name // Mặc định khi không tìm thấy id nào khớp
        }

        textView.text = getString(titleResId)
    }
}