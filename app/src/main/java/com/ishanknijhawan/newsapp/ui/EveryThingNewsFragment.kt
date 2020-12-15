package com.ishanknijhawan.newsapp.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ishanknijhawan.newsapp.R
import com.ishanknijhawan.newsapp.adapter.LoadingNewsAdapter
import com.ishanknijhawan.newsapp.adapter.NewsAdapter
import com.ishanknijhawan.newsapp.data.News
import com.ishanknijhawan.newsapp.databinding.EveryThingNewsFragmentBinding
import com.ishanknijhawan.newsapp.viewmodel.EveryThingNewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EveryThingNewsFragment : Fragment(R.layout.every_thing_news_fragment),
    NewsAdapter.OnNewsItemClickListener {
    private val mViewModel by viewModels<EveryThingNewsViewModel>()
    private var _binding: EveryThingNewsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var bitmap: Bitmap
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    var checkedItem = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = EveryThingNewsFragmentBinding.bind(view)
        val adapter = NewsAdapter(this)
        bottomNavigation = requireActivity().findViewById(R.id.bottomNavigation)

        binding.apply {
            recyclerView = rvNews
            rvNews.setHasFixedSize(true)
            rvNews.itemAnimator = null
            rvNews.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadingNewsAdapter { adapter.retry() },
                footer = LoadingNewsAdapter { adapter.retry() }
            )
            btnRetryFragment.setOnClickListener {
                adapter.retry()
            }
        }

        //mViewModel.observeData()
        mViewModel.news.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })


        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressp.isVisible = loadState.source.refresh is LoadState.Loading
                rvNews.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetryFragment.isVisible = loadState.source.refresh is LoadState.Error
                tvNoResultsFragment.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvNews.isVisible = false
                    tvNoResultsFragment.isVisible = true
                } else {
                    tvNoResults.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = newConfig.orientation
        if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            bottomNavigation.visibility = View.VISIBLE
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
            bottomNavigation.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.app_bar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val sortItem = menu.findItem(R.id.action_sort)
        val filterItem = menu.findItem(R.id.action_filter)
        val searchView = searchItem.actionView as SearchView

        sortItem.setOnMenuItemClickListener {
            val singleItems = arrayOf("Published", "Relevance", "Popularity")

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sort by")
                .setPositiveButton("OK") { _, which ->
                    when (checkedItem) {
                        0 -> mViewModel.sortNewsArticles("publishedAt")
                        1 -> mViewModel.sortNewsArticles("relevancy")
                        2 -> mViewModel.sortNewsArticles("popularity")
                    }
                }
                .setSingleChoiceItems(singleItems, checkedItem) { _, which ->
                    checkedItem = which
                }
                .show()
            return@setOnMenuItemClickListener false
        }

        filterItem.setOnMenuItemClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.show(activity?.supportFragmentManager!!, picker.toString())
            picker.addOnNegativeButtonClickListener { dismiss() }
            picker.addOnPositiveButtonClickListener {
                val dateFrom = Date(it.first!!)
                val dateTo = Date(it.second!!)
                val df = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val dateTextFrom: String = df.format(dateFrom)
                val dateTextTo: String = df.format(dateTo)
                mViewModel.filterNewsArticles(dateTextFrom, dateTextTo)
            }
            return@setOnMenuItemClickListener false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    binding.rvNews.scrollToPosition(0)
                    mViewModel.searchNewsArticles(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onItemClick(news: News) {
        val url = news.url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onClickShare(news: News) {

        val detailText = """
            Check this out!
            ${news.title}
            Read more at ${news.url}
        """.trimIndent()
        try {
            Glide.with(requireContext())
                .asBitmap()
                .load(news.urlToImage)
                .into(object : CustomTarget<Bitmap?>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {
                        bitmap = resource
                        try {
                            val cachePath = File(requireContext().cacheDir, "images")
                            cachePath.mkdirs()
                            val stream =
                                FileOutputStream("$cachePath/image.png")
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            stream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val imagePath = File(requireContext().cacheDir, "images")
                        val newFile = File(imagePath, "image.png")
                        val imageUri =
                            FileProvider.getUriForFile(
                                requireContext(),
                                "${getString(R.string.appId)}.provider",
                                newFile
                            )
                        if (imageUri != null) {
                            val intent = Intent()
                            intent.action = Intent.ACTION_SEND
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.setDataAndType(
                                imageUri,
                                requireContext().contentResolver.getType(imageUri)
                            )
                            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
                            intent.putExtra(Intent.EXTRA_TEXT, detailText)
                            intent.type = "*/*"
                            startActivity(Intent.createChooser(intent, "Choose an app"))
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

        } catch (e: IOException) {
            Toast.makeText(context, "Error sharing the image", Toast.LENGTH_SHORT).show()
        }

    }

    override fun dismiss() {

    }
}