package com.example.audiobook

import android.os.Bundle
import android.widget.TextView
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import audiobook
import com.example.audiobook.databinding.ActivityMainScreenBinding
import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.ui.detail.DetailFragment
import com.example.audiobook.ui.home.HomeFragment


class MainScreen : AppCompatActivity(){

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var textView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView

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
        // Thêm listener để cập nhật BottomNavigationView khi chuyển đổi fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigationView(destination.id)
        }
        // Thêm listener để cập nhật title khi chuyển đổi fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateTitle(destination.id)
        }
        navView.setupWithNavController(navController)

    }
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val detailFragment = fragmentManager.findFragmentByTag("DetailFragment")
        val containerFragment = fragmentManager.findFragmentById(R.id.container)

        // Kiểm tra xem có DetailFragment hiện đang được hiển thị hay không
        if (detailFragment != null && detailFragment.isVisible) {
            // Hiển thị lại Fragment có id là container khi ấn nút back
            val transaction = fragmentManager.beginTransaction()
            transaction.remove(detailFragment)
            transaction.show(containerFragment!!)
            transaction.commit()
        } else {
            super.onBackPressed()
        }
    }
    private fun updateBottomNavigationView(destinationId: Int?) {
        val hiddenDestinations = setOf(
            R.id.navigation_detailPage
            // Thêm ID của các fragment mà bạn muốn ẩn BottomNavigationView ở đây
        )
        bottomNavigationView = findViewById(R.id.nav_view)
        if (hiddenDestinations.contains(destinationId)) {
            bottomNavigationView.visibility = View.GONE
        } else {
            bottomNavigationView.visibility = View.VISIBLE
        }
    }
    private fun updateTitle(destinationId: Int?) {
        val titleResId = when (destinationId) {
            R.id.navigation_home -> R.string.title_home
            R.id.navigation_search -> R.string.title_search
            R.id.navigation_music -> R.string.title_music
            R.id.navigation_profile -> R.string.title_profile
            R.id.navigation_detailPage ->R.string.title_Detail
            else -> R.string.app_name // Mặc định khi không tìm thấy id nào khớp
        }

        textView.text = getString(titleResId)
    }
}