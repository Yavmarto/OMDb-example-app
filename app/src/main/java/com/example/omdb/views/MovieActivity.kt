package com.example.omdb.views

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.omdb.R
import com.example.omdb.models.MovieModel
import com.squareup.picasso.Picasso

/**
 * Movie activity
 */
class MovieActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        // Selected movie
        var selectedMovieModel = intent.getSerializableExtra(MOVIEKEY) as MovieModel

        // Get views
        val poster = findViewById<ImageView>(R.id.movie_image)
        val title = findViewById<TextView>(R.id.movie_title)
        val year = findViewById<TextView>(R.id.movie_year)
        val director = findViewById<TextView>(R.id.movie_director)
        val rating = findViewById<TextView>(R.id.movie_rating)
        val details = findViewById<TextView>(R.id.movie_details)
        val cast = findViewById<TextView>(R.id.movie_cast)

        // Set views
        Picasso.get().load(selectedMovieModel?.posterUrl).into(poster)
        title.text = selectedMovieModel?.title
        year.text = selectedMovieModel?.year
        director.text = selectedMovieModel?.director
        rating.text = selectedMovieModel?.imdbRating
        details.text = selectedMovieModel?.details
        cast.text = selectedMovieModel?.cast
    }

    companion object {
        private val MOVIEKEY = "MOVIE"
    }
}