package com.example.myapplication
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*



class ViewRatingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RatingsAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ratings)

        recyclerView = findViewById(R.id.recyclerViewRatings)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RatingsAdapter()
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("Reviews")

        loadRatings()
    }

    private fun loadRatings() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ratingsList = mutableListOf<RatingItem>()
                for (reviewSnapshot in dataSnapshot.children) {
                    val reviewId = reviewSnapshot.key.toString()
                    val rating = reviewSnapshot.child("rating").getValue(Int::class.java)
                    val comments = reviewSnapshot.child("comments").getValue(String::class.java)
                    val userId = reviewSnapshot.child("userId").getValue(String::class.java)

                    if (rating != null && userId != null) {
                        val ratingItem = RatingItem(reviewId, rating, comments ?: "", userId)
                        ratingsList.add(ratingItem)
                    }
                }
                adapter.setRatings(ratingsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}
