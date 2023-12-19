package com.example.audiobook.ui.detail

import android.app.DownloadManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import audiobook
import com.example.audiobook.DetailAdapter
import com.example.audiobook.R
import com.example.audiobook.databinding.FragmentDetailBinding
import com.example.audiobook.play_book
import com.example.audiobook.ui.music.MusicFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DetailFragment : Fragment(), DetailAdapter.OnItemClickListener,DetailAdapter.OnFavoriteItemClickListener,DetailAdapter.OnFavoriteItemRemoveListener,DetailAdapter.OnWaitItemClickListener,DetailAdapter.OnWaitItemRemoveListener,DetailAdapter.OnDownloadItemClickListener{

    private lateinit var binding: FragmentDetailBinding
    private lateinit var detailListView1: RecyclerView
    private val sharedViewModel: ViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
            .visibility = View.GONE
        val backButton = view.findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // Thực hiện logic khi ấn nút back

            requireActivity().supportFragmentManager.popBackStack()
            (requireActivity() as AppCompatActivity).findViewById<BottomNavigationView>(R.id.nav_view)
                .visibility = View.VISIBLE
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
                setupRecyclerView(detailListView1, "allAudio")
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
        // Thêm sự kiện cho menu item xoá

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
        adapter.setOnFavoriteItemClickListener(this)
        adapter.setOnFavoriteItemRemoveListener(this)
        adapter.setOnWaitItemClickListener(this)
        adapter.setOnWaitItemRemoveListener(this)
        adapter.setOnDownloadItemClickListnener(this)
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

        sharedViewModel.addToHistory(audiobook)
        startActivity(intent)
    }

    override fun onWaitItemClick(item: audiobook) {
        if (isItemInWait(item)) {
            Toast.makeText(
                requireContext(),
                "Bài này đã được thêm vào danh sách phát rồi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val waitItem = audiobook(
                name = item.name,
                image = item.image,
                type = item.type,
                author = item.author,
                file = item.file,
                detailPageType = item.detailPageType
            )

            // Set the updated favoritesList in the ViewModel
            sharedViewModel.addToWait(waitItem)
            Toast.makeText(
                requireContext(),
                "Đã thêm thành công vào danh sách phát",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onWaitItemRemove(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "Bạn không thể xoá item trong danh sách phát ở đây, hãy chuyển đến Profile Fragment",
            Toast.LENGTH_SHORT
        ).show()
    }
    override fun onFavoriteItemRemove(item: audiobook) {
        Toast.makeText(
            requireContext(),
            "You cannot remove favorite items from this page. Please go back to the Music page",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDownloadItemClick(item: audiobook) {
        downloadFile(item.file, "${item.name}_${item.author}.mp3")
    }
    private fun downloadFile(url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading")
            .setDescription("Downloading $fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        // Đặt đường dẫn đến thư mục downloads của ứng dụng

        // Cho phép download qua mạng di động và roaming
        request.setAllowedOverMetered(true)
        request.setAllowedOverRoaming(true)

        val downloadManager = requireActivity().getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(requireContext(), "Đang tải xuống $fileName", Toast.LENGTH_SHORT).show()
    }

    override fun onFavoriteItemClick(item: audiobook) {
        if (isItemInFavorites(item)) {
            Toast.makeText(
                requireContext(),
                "Bài này đã được thêm vào danh sách yêu thích rồi",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val favoriteItem = audiobook(
                name = item.name,
                image = item.image,
                type = item.type,
                author = item.author,
                file = item.file,
                detailPageType = item.detailPageType
            )

            // Set the updated favoritesList in the ViewModel
            sharedViewModel.addToFavorites(favoriteItem)
            Toast.makeText(
                requireContext(),
                "Đã thêm thành công vào danh sách yêu thích",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun isItemInFavorites(item: audiobook): Boolean {
        val favoritesList = sharedViewModel.favoritesLiveData.value
        return favoritesList?.any {
            it.name == item.name && it.author == item.author
        } == true
    }
    private fun isItemInWait(item: audiobook): Boolean {
        val waitList = sharedViewModel.waitData.value
        return waitList?.any {
            it.name == item.name && it.author == item.author
        } == true
    }
}
