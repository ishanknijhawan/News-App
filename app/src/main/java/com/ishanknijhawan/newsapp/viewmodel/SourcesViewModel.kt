package com.ishanknijhawan.newsapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ishanknijhawan.newsapp.repository.NewsRepository

class SourcesViewModel @ViewModelInject constructor(repository: NewsRepository) :
    ViewModel() {
}