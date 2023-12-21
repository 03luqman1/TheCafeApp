package com.example.myapplication

import androidx.lifecycle.ViewModel

class BasketViewModel : ViewModel() {
    val basketItems: MutableList<String> = mutableListOf()
}
