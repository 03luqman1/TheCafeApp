package com.example.myapplication

data class PaymentDetails(
    val paymentId: String,
    val orderId: String?,
    val paymentType: String,
    val amount: Double,
    val paymentDate: String
)
