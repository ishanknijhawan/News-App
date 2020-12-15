package com.ishanknijhawan.newsapp.ui

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ishanknijhawan.newsapp.R
import com.ishanknijhawan.newsapp.adapter.SourceAdapter
import com.ishanknijhawan.newsapp.api.SourceClient
import com.ishanknijhawan.newsapp.data.Source
import com.ishanknijhawan.newsapp.databinding.SourcesFragmentBinding
import com.ishanknijhawan.newsapp.databinding.TopHeadlinesFragmentBinding
import com.ishanknijhawan.newsapp.response.SourceResponse
import com.ishanknijhawan.newsapp.viewmodel.SourcesViewModel
import com.ishanknijhawan.newsapp.viewmodel.TopHeadlinesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SourcesFragment : Fragment(R.layout.sources_fragment),
    SourceAdapter.OnSourceItemClickListener {
    private var _binding: SourcesFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var sources: List<Source>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SourcesFragmentBinding.bind(view)

        checkForResults()
        binding.btnRetry.setOnClickListener {
            checkForResults()
        }
    }

    private fun checkForResults() {
        if (internetAvailable()) {
            fetchResults()
        } else {
            binding.tvNoResults.visibility = View.VISIBLE
            binding.btnRetry.visibility = View.VISIBLE
            binding.progressp.visibility = View.GONE
        }
    }

    //check if internet connection is available
    @Throws(InterruptedException::class, IOException::class)
    fun internetAvailable(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    private fun fetchResults() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { SourceClient.api.clone().execute() }
            if (response.isSuccessful && response.code == 200) {
                binding.tvNoResults.visibility = View.GONE
                binding.btnRetry.visibility = View.GONE

                val data = Gson().fromJson(response.body?.string(), SourceResponse::class.java)
                launch(Dispatchers.Main) {

                    sources = data.sources
                    binding.apply {
                        progressp.visibility = View.GONE
                        rvSources.layoutManager = LinearLayoutManager(requireContext())
                        rvSources.adapter = SourceAdapter(sources, this@SourcesFragment)
                    }
                }
            } else {
                binding.tvNoResults.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClick(source: Source) {
        val url = source.url
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}