package com.example.myapplication

data class Order(
    val orderId: String,
    val customerId: String?,
    val orderDateTime: String,
    val orderStatus: String
)