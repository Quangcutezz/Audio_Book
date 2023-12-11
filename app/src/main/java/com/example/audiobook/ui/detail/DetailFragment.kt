package com.example.audiobook.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.DetailAdapter
import com.example.audiobook.R
import com.example.audiobook.databinding.FragmentDetailBinding
import com.example.audiobook.play_book
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DetailFragment : Fragment(), DetailAdapter.OnItemClickListener {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailListView1: RecyclerView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        detailListView1 = binding.detailList1

        val arguments = arguments

        val audiobook = arguments?.getSerializable(ARG_AUDIOBOOK) as audiobook?
        val detailPageType = audiobook?.detailPageType
        if (detailPageType != null) {

            // Sử dụng giá trị detailPageType để xác định dữ liệu cần hiển thị
            if ("type1" == detailPageType) {
                // Hiển thị dữ liệu từ database DetailPage1
                setupRecyclerView(detailListView1, "DetailAudio")
            } else if ("type2" == detailPageType) {
                // Hiển thị dữ liệu từ database DetailPage2
                setupRecyclerView(detailListView1, "DetailAudio2")
            }
            else if ("type3" == detailPageType) {
                // Hiển thị dữ liệu từ database DetailPage2
                setupRecyclerView(detailListView1, "DetailAudio3")
            }
            // ...
        }
        val image = audiobook?.image
        val intro = audiobook?.name
        // Set the background of bg_topDetail using Picasso
        //Picasso.get().load(image).into(binding.bgTopDetail)
        // Inside your Fragment or Activity
        val introText = binding.introText
        introText.setText(intro)
        val imageViewBackground = binding.imageViewBackground

        // Load the image into the ImageView using Picasso
        Picasso.get().load(image).into(imageViewBackground)

        //setupRecyclerView(detailListView1, "DetailAudio")

        return root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, databasePath: String) {
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = when (databasePath) {
            "DetailAudio" -> DetailAdapter(emptyList())
            else -> DetailAdapter(emptyList())
        }

        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        val databaseReference = FirebaseDatabase.getInstance().reference.child(databasePath)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val genres = mutableListOf<audiobook>()
                for (dataSnapshot in snapshot.children) {
                    val image = dataSnapshot.child("image").getValue(String::class.java) ?: ""
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    val author = dataSnapshot.child("author").getValue(String::class.java) ?: ""
                    val type = dataSnapshot.child("type").getValue(String::class.java) ?: ""
                    val file = dataSnapshot.child("file").getValue(String::class.java) ?: ""
                    val detailPageType = dataSnapshot.child("detailPageType").getValue(String::class.java) ?: ""
                    val genre = audiobook(name, image, type, author, file,detailPageType)
                    genres.add(genre)
                }
                adapter.setData(genres)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onItemClick(audiobook: audiobook) {
        val intent = Intent(requireContext(), play_book::class.java)
        intent.putExtra("IMAGE", audiobook.image)
        intent.putExtra("NAME", audiobook.name)
        intent.putExtra("AUTHOR", audiobook.author)
        intent.putExtra("FILE", audiobook.file)
        startActivity(intent)
    }
}
