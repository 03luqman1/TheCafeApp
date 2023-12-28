package com.example.myapplication

data class Order(
    val orderId: String = "",
    val customerId: String? = null,
    val orderDateTime: String = "",
    val orderStatus: String = ""
)