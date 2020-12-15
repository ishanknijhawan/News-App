package com.ishanknijhawan.newsapp.custom_switchmap

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class MultipleSwitchMapTrigger<A, B, C, D>(
    a: MutableLiveData<A>,
    b: MutableLiveData<B>,
    c: Pair<MutableLiveData<C>,MutableLiveData<D>>
) :
    MediatorLiveData<Triple<A?, B?, Pair<C?, D?>>>() {
    init {
        addSource(a) {
            value = Triple(it, b.value, Pair(c.first.value, c.second.value))
        }
        addSource(b) {
            value = Triple(a.value, it, Pair(c.first.value, c.second.value))
        }
        addSource(c.first) {
            value = Triple(a.value, b.value, Pair(it, c.second.value))
        }
        addSource(c.second) {
            value = Triple(a.value, b.value, Pair(c.first.value, it))
        }
    }
}