package com.example.audiobook.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import audiobook
import com.example.audiobook.R
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
            .visibility = View.GONE
        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Thực hiện logic khi ấn nút back
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
                .visibility = View.VISIBLE
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
    companion object {
        private const val ARG_AUDIOBOOK = "arg_audiobook"

        fun newInstance(audiobook: audiobook): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_AUDIOBOOK, audiobook)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)

    }


}