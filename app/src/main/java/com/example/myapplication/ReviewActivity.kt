package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReviewActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Get references to the views
        val ratingBar: RatingBar = findViewById(R.id.ratingBar)
        val editTextComments: EditText = findViewById(R.id.editTextComments)
        val buttonSubmitReview: Button = findViewById(R.id.buttonSubmitReview)

        // Set up click listener for the "Submit Review" button
        buttonSubmitReview.setOnClickListener {
            // Get the selected rating and comments
            val rating: Float = ratingBar.rating
            val comments: String = editTextComments.text.toString()

            // Display a toast with the review details (replace with your actual logic)
            showToast("Rating: $rating\nComments: $comments")

            // Add review details to the Realtime Database
            addReviewToDatabase(rating, comments)

            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun addReviewToDatabase(rating: Float, comments: String) {
        val user = auth.currentUser
        val userId = user?.uid

        userId?.let {
            val database = Firebase.database
            val reviewsRef = database.getReference("reviews")

            // Create a unique key for each review
            val reviewId = reviewsRef.push().key

            // Create a Review object
            val review = Review(userId, rating, comments)

            // Push the review to the "reviews" node using the unique key as the identifier
            reviewId?.let {
                reviewsRef.child(it).setValue(review)
            }
        }
    }

    private fun showToast(message: String) {
        // Helper function to show Toast messages
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
