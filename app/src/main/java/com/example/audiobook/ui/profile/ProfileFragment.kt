package com.example.audiobook.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.audiobook.R
import com.example.audiobook.databinding.FragmentHomeBinding
import com.example.audiobook.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        // Tắt hiển thị tiêu đề
//        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        return root
    }


}