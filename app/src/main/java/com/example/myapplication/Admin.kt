package com.example.myapplication

class Admin {
    private var email: String? = null
    private var fullName: String? = null
    private var phoneNumber: String? = null
    private var userName: String? = null


    constructor() {}
    constructor(email: String?, fullName: String?, phoneNumber: String?, userName: String?) {
        this.email = email
        this.fullName = fullName
        this.phoneNumber = phoneNumber
        this.userName = userName
    } // Add getters and setters as needed
}
